package org.sp.admin.component;

import jakarta.annotation.Resource;
import org.sp.admin.security.PermissionCacheService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * 程序预启动
 */
@Component
public class AppListener {


    @Resource
    private PermissionCacheService permissionCacheService;


    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {


        this.permissionCacheService.genAuthorities();

        this.permissionCacheService.gentRootAuthorities();


    }
}
