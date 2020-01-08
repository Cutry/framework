package com.bluesky.framework.boot;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.bluesky.framework.FileUpload.FileUploadManager;
import com.bluesky.framework.SysParam.SysParamManager;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.auth.AccountAuthorityManager;
import com.bluesky.framework.account.auth.RoleAuthorityManager;
import com.bluesky.framework.account.log.AccountLoginLogManager;
import com.bluesky.framework.account.log.AccountOperateLogManager;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.article.ArticleManager;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.PageSettingManager;
import org.mvnsearch.spring.boot.dubbo.DubboAutoConfiguration;
import org.mvnsearch.spring.boot.dubbo.DubboBasedAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * auto configuration
 */
@Configuration
@EnableConfigurationProperties(FrameworkProperties.class)
@AutoConfigureAfter(DubboAutoConfiguration.class)
public class FrameworkAutoConfiguration extends DubboBasedAutoConfiguration {
    @Autowired
    private FrameworkProperties properties;

    /**
     * setting
     **/
    @Bean
    public ReferenceBean pageSettingManager() {
        return getConsumerBean(PageSettingManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean menuSourceManager() {
        return getConsumerBean(MenuSourceManager.class, properties.getVersion(), properties.getTimeout());
    }

    /**
     * account
     **/
    @Bean
    public ReferenceBean accountManager() {
        return getConsumerBean(AccountManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean regionManager() {
        return getConsumerBean(RegionManager.class, properties.getVersion(), properties.getTimeout());
    }


    @Bean
    public ReferenceBean organizationManager() {
        return getConsumerBean(OrganizationManager.class, properties.getVersion(), properties.getTimeout());
    }


    @Bean
    public ReferenceBean roleManager() {
        return getConsumerBean(RoleManager.class, properties.getVersion(), properties.getTimeout());
    }


    @Bean
    public ReferenceBean accountLoginLogManager() {
        return getConsumerBean(AccountLoginLogManager.class, properties.getVersion(), properties.getTimeout());
    }


    @Bean
    public ReferenceBean accountOperateLogManager() {
        return getConsumerBean(AccountOperateLogManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean roleAuthorityManager() {
        return getConsumerBean(RoleAuthorityManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean accountAuthorityManager() {
        return getConsumerBean(AccountAuthorityManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean sysParamManager(){
        return getConsumerBean(SysParamManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean fileUploadManager(){
        return getConsumerBean(FileUploadManager.class, properties.getVersion(), properties.getTimeout());
    }

    @Bean
    public ReferenceBean articleManager(){
        return getConsumerBean(ArticleManager.class, properties.getVersion(), properties.getTimeout());
    }

}