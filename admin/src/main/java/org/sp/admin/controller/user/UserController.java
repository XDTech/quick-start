package org.sp.admin.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.StaticLog;
import jakarta.annotation.Resource;
import org.sp.admin.beans.ResponseBean;
import org.sp.admin.beans.UserBean;
import org.sp.admin.enums.ResponseEnum;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.security.UserSecurity;
import org.sp.admin.service.user.UserService;
import org.sp.admin.utils.SecurityUtils;
import org.sp.admin.validation.user.UserVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

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
    @Autowired
    private UserService mUserService;

    @Resource
    private UserSecurity userSecurity;


    private final SecurityUtils securityUtils = new SecurityUtils();

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("/page/list")
    public ResponseEntity<?> getMUserPageList(@RequestParam Integer pi, @RequestParam Integer ps) {


        Page<UserModel> mUserList = this.mUserService.getMUserPageList(pi, ps);

        return ResponseEntity.ok(ResponseBean.createResponseBean(ResponseEnum.Success.getCode(), mUserList.getTotalElements(), mUserList.getContent(), ResponseEnum.Success.getMsg()));


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

        UserBean userBean=new UserBean();
        BeanUtil.copyProperties(mUser.get(),userBean);
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

        UserModel user = this.mUserService.getUser(userBean.getUsername());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("数据不存在");
        }

        UserModel mUser = mUserOptional.get();
        BeanUtil.copyProperties(userBean, mUser, "createdAt", "updatedAt", "status", "deleted");


        return ResponseEntity.ok(this.mUserService.updateMUser(mUser));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("数据不存在");
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

}

