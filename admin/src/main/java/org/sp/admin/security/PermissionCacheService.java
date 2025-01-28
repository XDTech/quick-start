package org.sp.admin.security;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Struct;
import org.sp.admin.model.system.PermissionModel;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.model.system.RolePermissionModel;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.service.system.PermissionService;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.service.user.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Date:2025/01/27 20:59:33
 * Author：Tobin
 * Description:
 */

@Service
@Slf4j
public class PermissionCacheService {


    private final String ROLE_PERMISSION_KEY = "role_permission:";
    @Resource
    private RoleService roleService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RedisTemplate<String, List<String>> redisTemplate;

    @Resource
    private UserService userService;


    // 把所有的角色权限放到缓存里
    public void genAuthorities() {


        List<RoleModel> roleList = this.roleService.getRoleList();

        for (RoleModel roleModel : roleList) {

            if (roleModel.getIdentity().equals("root")) continue;
            // 查询角色权限
            List<RolePermissionModel> rolePermissions = this.roleService.getRolePermissions(roleModel.getId());

            // 放到缓存里

            if (ObjectUtil.isEmpty(rolePermissions)) continue;


            // 查询权限

            List<Long> permissionIds = rolePermissions.stream().map(RolePermissionModel::getPermissionId).toList();

            List<PermissionModel> permissionModels = this.permissionService.getPermissionList(permissionIds);

            List<String> permission = permissionModels.stream().map(PermissionModel::getIdentity).toList();


            this.redisTemplate.opsForValue().set(this.genKey(roleModel.getId()), permission);

        }


    }


    // 生成root账户的全部权限
    public void gentRootAuthorities() {

        List<PermissionModel> permissionListAll = this.permissionService.getPermissionListAll();

        List<String> identityList = permissionListAll.stream().map(PermissionModel::getIdentity).toList();


        // 查询root账户
        RoleModel root = this.roleService.getRole("root");

        if (ObjectUtil.isNotEmpty(root)) {
            // 生成root账户的权限
            this.redisTemplate.opsForValue().set(this.genKey(root.getId()), identityList);

        }


    }


    public List<String> getGrantedAuthorities(Long roleId) {

        List<String> authList = new ArrayList<>();

        List<String> authObject = this.redisTemplate.opsForValue().get(this.genKey(roleId));
        Console.log(authObject);

        if (authObject == null) {
            return authList;
        }

        return authObject;


    }


    public String genKey(Long roleId) {
        return StrUtil.format("{}{}", this.ROLE_PERMISSION_KEY, roleId);
    }
}


