package org.sp.admin.security;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.service.user.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 自定义权限验证接口扩展
 */
@Component
public class PermissionInterfaceImpl implements StpInterface {





    @Resource
    private UserSecurity userSecurity;


    @Resource
    private UserService userService;


    @Resource
    private RoleService roleService;

    // 返回一个账号所拥有的权限码集合
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        // 1. 声明权限码集合
        List<String> permissionList = new ArrayList<>();

        // 如果是root账户，读取全部权限


        // 3. 遍历角色列表，查询拥有的权限码
        for (String roleId : getUserRole(loginId, loginType)) {
            List<String> grantedAuthorities = this.roleService.getGrantedAuthorities(Convert.toLong(roleId));

            permissionList.addAll(grantedAuthorities);
        }

        // 3. 返回权限码集合
        return permissionList;
    }


    private List<String> getUserRole(Object loginId, String loginType) {
        List<String> roleList = new ArrayList<>();
        UserModel currentAdminUser = this.userSecurity.getCurrentAdminUser();

        if (currentAdminUser.getStatus().equals(StatusEnum.locked)) return roleList;


        Long[] roles = currentAdminUser.getRoles();

        roleList = Arrays.stream(roles).map(String::valueOf).collect(Collectors.toList());


        return roleList;
    }

    // 返回一个账号所拥有的角色标识集合 多role
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 获取用户角色

        UserModel currentAdminUser = this.userSecurity.getCurrentAdminUser();


        return this.userService.getUserRoles(currentAdminUser);


    }

}