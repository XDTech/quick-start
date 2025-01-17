package org.sp.admin.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import jakarta.annotation.Resource;
import org.sp.admin.beans.ResponseBean;
import org.sp.admin.enums.StatusEnum;
import org.sp.admin.model.user.UserModel;
import org.sp.admin.security.UserSecurity;
import org.sp.admin.service.user.UserService;
import org.sp.admin.utils.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
@Validated
public class LoginController {

    @Resource
    private UserSecurity userSecurity;

    @Resource
    private UserService userService;

    private final SecurityUtils securityUtils = new SecurityUtils();

    @PostMapping("/user")
    public ResponseBean userLogin(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        UserModel user = this.userService.getUser(username);

        if (user == null) {
            return ResponseBean.fail("用户不存在");
        }
        // 验证密码
        if (!this.securityUtils.shaEncode(password + user.getSalt()).equals(user.getPassword())) {
            return ResponseBean.fail("账号/密码错误");
        }
        // 判断是否锁定

        if (user.getStatus().equals(StatusEnum.locked)) {
            return ResponseBean.fail("账号已被锁定");
        }

        SaTokenInfo login = this.userSecurity.login(user);
        return ResponseBean.success(login);
    }


}
