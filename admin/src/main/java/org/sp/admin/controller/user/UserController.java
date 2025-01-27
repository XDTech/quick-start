package org.sp.admin.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import jakarta.annotation.Resource;
import org.sp.admin.beans.ResponseBean;
import org.sp.admin.beans.UserBean;
import org.sp.admin.enums.ResponseEnum;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.model.system.DepartmentModel;
import org.sp.admin.model.system.RoleModel;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.security.PermissionCacheService;
import org.sp.admin.security.UserSecurity;
import org.sp.admin.service.system.DepartmentService;
import org.sp.admin.service.system.PermissionService;
import org.sp.admin.service.system.RoleService;
import org.sp.admin.service.user.UserService;
import org.sp.admin.utils.BeanConverterUtil;
import org.sp.admin.utils.SecurityUtils;
import org.sp.admin.validation.user.UserVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (UserModel)表控制层
 *
 * @author Tobin
 * @since 2024-11-10 10:05:25
 */
@RestController
@RequestMapping("/user")
@Validated  //单参数校验时我们需要，在方法的类上加上@Validated注解，否则校验不生效。
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService mUserService;

    @Resource
    private UserSecurity userSecurity;


    @Resource
    private DepartmentService departmentService;

    @Resource
    private RoleService roleService;


    @Resource
    private PermissionCacheService permissionCacheService;

    private final SecurityUtils securityUtils = new SecurityUtils();

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("/page/list")
    public ResponseEntity<?> getMUserPageList(@RequestParam Integer pi, @RequestParam Integer ps, @RequestParam(required = false) String name) {


        Page<UserModel> mUserList = this.mUserService.getMUserPageList(pi, ps, name);


        List<DepartmentModel> departmentList = this.departmentService.getDepartmentList(null);

        Map<Long, DepartmentModel> departmentModelMap = departmentList.stream().collect(Collectors.toMap(DepartmentModel::getId, s -> s));


        List<RoleModel> roleList = this.roleService.getRoleList(null);

        Map<Long, RoleModel> roleModelMap = roleList.stream().collect(Collectors.toMap(RoleModel::getId, s -> s));


        List<UserBean> userBeans = BeanConverterUtil.convertList(mUserList.getContent(), UserBean.class);


        for (UserBean userBean : userBeans) {
            DepartmentModel departmentModel = departmentModelMap.get(userBean.getDepartmentId());
            if (ObjectUtil.isNotEmpty(departmentModel)) {
                userBean.setDepartmentName(departmentModel.getName());
            }

            // 加载角色

            if (userBean.getRoles() == null) continue;
            for (Long role : userBean.getRoles()) {
                RoleModel roleModel = roleModelMap.get(role);

                if (ObjectUtil.isNotEmpty(roleModel)) {
                    userBean.getRoleNames().add(roleModel.getName());
                }

            }

        }
        return ResponseEntity.ok(ResponseBean.success(mUserList.getTotalElements(), userBeans));


    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMUser(@PathVariable("id") Long id) {

        Optional<UserModel> mUser = this.mUserService.getMUser(id);
        if (mUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("数据不存在");
        }

        UserBean userBean = new UserBean();
        BeanUtil.copyProperties(mUser.get(), userBean);
        return ResponseEntity.ok(ResponseBean.success(userBean));

    }

    /**
     * 新增数据
     *
     * @param userBean 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<?> createMUser(@RequestBody @Validated(UserVal.Create.class) UserVal userBean) {

        StaticLog.info("{}", userBean.toString());

        UserModel user = this.mUserService.getUser(userBean.getUsername().trim());
        if (user != null) {
            return ResponseEntity.ok(ResponseBean.fail("用户已存在"));
        }
        UserModel newUser = new UserModel();


        BeanUtil.copyProperties(userBean, newUser);


        // 生成加密盐
        String salt = this.securityUtils.createSecuritySalt();

        newUser.setSalt(salt);
        // 生成密码

        String pwd = this.securityUtils.shaEncode(newUser.getPassword() + salt);

        newUser.setPassword(pwd);
        UserModel userPojo = this.mUserService.createMUser(newUser);
        // 序列化返回
        UserBean newAminUserBean = new UserBean();
        BeanUtil.copyProperties(userPojo, newAminUserBean);

        return ResponseEntity.ok(ResponseBean.success(newAminUserBean));
    }

    /**
     * 编辑数据
     *
     * @param userBean 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<?> updateMUser(@RequestBody @Validated(UserVal.Update.class) UserVal userBean) {

        Optional<UserModel> mUserOptional = this.mUserService.getMUser(userBean.getId());
        if (mUserOptional.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail());
        }

        UserModel mUser = mUserOptional.get();
        mUser.setAvatar(userBean.getAvatar());
        mUser.setDepartmentId(userBean.getDepartmentId());
        mUser.setEmail(userBean.getEmail());
        mUser.setName(userBean.getName());
        mUser.setPhone(userBean.getPhone());
        mUser.setPostName(userBean.getPostName());
        mUser.setRoles(userBean.getRoles());
        mUser.setStatus(StatusEnum.valueOf(userBean.getStatus()));
        this.mUserService.updateMUser(mUser);

        return ResponseEntity.ok(ResponseBean.success());
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMUserById(@PathVariable("id") Long id) {

        Optional<UserModel> mUser = this.mUserService.getMUser(id);
        if (mUser.isEmpty()) {
            return ResponseEntity.ok(ResponseBean.fail("用户不存在"));
        }

        this.mUserService.deleteMUser(mUser.get());
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前登录用户的用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {

        UserModel currentAdminUser = this.userSecurity.getCurrentAdminUser();


        UserBean userBean = new UserBean();

        BeanUtil.copyProperties(currentAdminUser, userBean);


        return ResponseEntity.ok(ResponseBean.success(userBean));
    }


    // 修改指定账号的username
    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@RequestParam Long id, @RequestParam String username) {


        Optional<UserModel> mUser = this.mUserService.getMUser(id);

        if (mUser.isEmpty()) return ResponseEntity.ok(ResponseBean.fail("账户不存在"));


        // 查询账号是否存在
        UserModel user = this.mUserService.getUser(username.trim());

        if (ObjectUtil.isNotEmpty(user) && !user.getId().equals(mUser.get().getId())) {
            return ResponseEntity.ok(ResponseBean.fail("账户已存在"));
        }


        mUser.get().setUsername(username);
        this.mUserService.updateMUser(mUser.get());

        return ResponseEntity.ok(ResponseBean.success());
    }


    @PutMapping("/pwd")
    public ResponseEntity<?> updatePwd(@RequestParam Long id, @RequestParam String pwd) {


        Optional<UserModel> mUser = this.mUserService.getMUser(id);

        if (mUser.isEmpty()) return ResponseEntity.ok(ResponseBean.fail("账户不存在"));


        this.mUserService.updateMUser(this.mUserService.genPwd(mUser.get(), pwd));

        return ResponseEntity.ok(ResponseBean.success());
    }


    // 获取用户权限


    @GetMapping("/codes")
    public ResponseEntity<?> getUserCodes() {


        UserModel currentAdminUser = this.userSecurity.getCurrentAdminUser();

        // 用户角色码
        List<String> userRoles = this.mUserService.getUserRoles(currentAdminUser);


        //
        List<RoleModel> roleListIn = this.roleService.getRoleListIn(currentAdminUser.getRoles());


        Console.log(roleListIn.toString());
        for (RoleModel roleModel : roleListIn) {
            List<String> grantedAuthorities = this.permissionCacheService.getGrantedAuthorities(roleModel.getId());

            Console.log("auth:{}",grantedAuthorities.toString());

            userRoles.addAll(grantedAuthorities);
        }


        return ResponseEntity.ok(ResponseBean.success(userRoles));


    }

}

