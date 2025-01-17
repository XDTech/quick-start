package org.sp.admin.security;

import cn.dev33.satoken.stp.StpLogic;

public class StpKit {


    // 管理租户登录信息
    public static final StpLogic USER = new StpLogic("user");

    // 管理后台账户信息(暂未用到)
    @Deprecated
    public static final StpLogic ADMIN = new StpLogic("admin");
}
