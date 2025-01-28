package org.sp.admin.component;

import jakarta.annotation.Resource;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.service.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * 程序预启动
 */
@Component
public class AppListener {




    @Resource
    private UserService userService;


    @Resource
    private RoleService roleService;


    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {


        this.userService.createRootAccount();

        this.roleService.genAuthorities();

        this.roleService.gentRootAuthorities();


    }
}
