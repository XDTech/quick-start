package org.sp.admin.security;

import cn.dev33.satoken.stp.StpLogic;

public class StpKit {



    public static  final String USER_KEY="user";

    // 管理租户登录信息
    public static final StpLogic USER = new StpLogic(USER_KEY);

    // 管理后台账户信息(暂未用到)
    @Deprecated
    public static final StpLogic ADMIN = new StpLogic("admin");
}
