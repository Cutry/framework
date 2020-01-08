package com.bluesky.framework.account.boot.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * WebSecurityExpressionHandler
 *
 * @author linjiang
 */
public class MyWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {

    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @Override
    protected PermissionEvaluator getPermissionEvaluator() {
        return this.permissionEvaluator;
    }

}
