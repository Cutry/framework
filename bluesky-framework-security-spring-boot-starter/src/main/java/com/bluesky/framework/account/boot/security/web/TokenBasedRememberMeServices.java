package com.bluesky.framework.account.boot.security.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;


/**
 * 重写TokenBasedRememberMeServices
 */
public class TokenBasedRememberMeServices extends AbstractRememberMeServices {

    private String cookieDomain;

    private Boolean useSecureCookie = null;

    private Method setHttpOnlyMethod;


    public TokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
        this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens,
                                                 HttpServletRequest request, HttpServletResponse response) {

        if (cookieTokens.length != 3) {
            throw new InvalidCookieException("Cookie token did not contain 3"
                    + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        long tokenExpiryTime;

        try {
            tokenExpiryTime = new Long(cookieTokens[1]).longValue();
        } catch (NumberFormatException nfe) {
            throw new InvalidCookieException(
                    "Cookie token[1] did not contain a valid number (contained '"
                            + cookieTokens[1] + "')");
        }

        if (isTokenExpired(tokenExpiryTime)) {
            throw new InvalidCookieException("Cookie token[1] has expired (expired on '"
                    + new Date(tokenExpiryTime) + "'; current time is '" + new Date()
                    + "')");
        }

        // Check the user exists.
        // Defer lookup until after expiry time checked, to possibly avoid expensive
        // database call.

        UserDetails userDetails = getUserDetailsService().loadUserByUsername(
                cookieTokens[0]);

        // Check signature of token matches remaining details.
        // Must do this after user lookup, as we need the DAO-derived password.
        // If efficiency was a major issue, just add in a UserCache implementation,
        // but recall that this method is usually only called once per HttpSession - if
        // the token is valid,
        // it will cause SecurityContextHolder population, whilst if invalid, will cause
        // the cookie to be cancelled.
        String expectedTokenSignature = makeTokenSignature(tokenExpiryTime,
                userDetails.getUsername(), userDetails.getPassword());

        if (!equals(expectedTokenSignature, cookieTokens[2])) {
            throw new InvalidCookieException("Cookie token[2] contained signature '"
                    + cookieTokens[2] + "' but expected '" + expectedTokenSignature + "'");
        }

        //是否需要重新延长时效
        int tokenLifetime = getTokenValiditySeconds();
        long tokenLifetimeMillis = tokenLifetime * 1000 / 3;
        //是否已经过了2/3的时间了，比如30，是否过了第20分钟了，从20分钟开始重新设置cookies
        if (tokenExpiryTime - System.currentTimeMillis() <= tokenLifetimeMillis) {
            long expiryTime = System.currentTimeMillis();
            // SEC-949
            expiryTime += 1000L * ((tokenLifetime < 0) ? TWO_WEEKS_S : tokenLifetime);
            String signatureValue = makeTokenSignature(expiryTime, userDetails.getUsername(), userDetails.getPassword());
            setCookie(new String[]{userDetails.getUsername(), Long.toString(expiryTime), signatureValue},
                    tokenLifetime, request, response);
        }

        return userDetails;
    }

    /**
     * Calculates the digital signature to be put in the cookie. Default value is MD5
     * ("username:tokenExpiryTime:password:key")
     */
    protected String makeTokenSignature(long tokenExpiryTime, String username,
                                        String password) {
        String data = username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(data.getBytes())));
    }

    protected boolean isTokenExpired(long tokenExpiryTime) {
        return tokenExpiryTime < System.currentTimeMillis();
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication successfulAuthentication) {

        String username = retrieveUserName(successfulAuthentication);
        String password = retrievePassword(successfulAuthentication);

        // If unable to find a username and password, just abort as
        // TokenBasedRememberMeServices is
        // unable to construct a valid token in this case.
        if (!StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }

        if (!StringUtils.hasLength(password)) {
            UserDetails user = getUserDetailsService().loadUserByUsername(username);
            password = user.getPassword();

            if (!StringUtils.hasLength(password)) {
                logger.debug("Unable to obtain password for user: " + username);
                return;
            }
        }

        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        // SEC-949
        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

        String signatureValue = makeTokenSignature(expiryTime, username, password);

        setCookie(new String[]{username, Long.toString(expiryTime), signatureValue},
                tokenLifetime, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username
                    + "', expiry: '" + new Date(expiryTime) + "'");
        }
    }

    /**
     * Calculates the validity period in seconds for a newly generated remember-me login.
     * After this period (from the current time) the remember-me login will be considered
     * expired. This method allows customization based on request parameters supplied with
     * the login or information in the <tt>Authentication</tt> object. The default value
     * is just the token validity period property, <tt>tokenValiditySeconds</tt>.
     * <p>
     * The returned value will be used to work out the expiry time of the token and will
     * also be used to set the <tt>maxAge</tt> property of the cookie.
     * <p>
     * See SEC-485.
     *
     * @param request        the request passed to onLoginSuccess
     * @param authentication the successful authentication object.
     * @return the lifetime in seconds.
     */
    protected int calculateLoginLifetime(HttpServletRequest request,
                                         Authentication authentication) {
        return getTokenValiditySeconds();
    }

    protected String retrieveUserName(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }

    protected String retrievePassword(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getPassword();
        } else {
            if (authentication.getCredentials() == null) {
                return null;
            }
            return authentication.getCredentials().toString();
        }
    }

    private boolean isInstanceOfUserDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetails;
    }

    /**
     * Constant time comparison to prevent against timing attacks.
     */
    private static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        if (expectedBytes.length != actualBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expectedBytes.length; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }

    private static byte[] bytesUtf8(String s) {
        if (s == null) {
            return null;
        }
        return Utf8.encode(s);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request,
                             HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(super.getCookieName(), cookieValue);

        //remember-me 会话级别,删除setMaxAge源码修改为会话级别使得浏览器关闭失效
        //cookie.setMaxAge(maxAge);
        cookie.setPath(getCookiePath(request));
        if (this.cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        if (maxAge < 1) {
            cookie.setVersion(1);
        }

        if (useSecureCookie == null) {
            cookie.setSecure(request.isSecure());
        } else {
            cookie.setSecure(useSecureCookie);
        }

        if (setHttpOnlyMethod != null) {
            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
        } else if (logger.isDebugEnabled()) {
            logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
        }

        response.addCookie(cookie);
    }

    @Override
    public void setCookieDomain(String cookieDomain) {
        Assert.hasLength(cookieDomain, "Cookie domain cannot be empty or null");
        this.cookieDomain = cookieDomain;
    }

    @Override
    public void setUseSecureCookie(boolean useSecureCookie) {
        this.useSecureCookie = useSecureCookie;
    }

    /**
     * 删除remember-me的Cookie
     *
     * @param response HttpServletResponse
     */
    private void cancelCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie(super.getCookieName(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(cookieDomain);
        response.addCookie(cookie);
    }
}
