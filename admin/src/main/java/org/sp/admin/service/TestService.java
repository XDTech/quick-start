package org.sp.admin.service;

import cn.hutool.core.lang.Console;
import org.sp.admin.annotation.DistributedLocker;
import org.sp.admin.beans.ResponseBean;
import org.springframework.stereotype.Service;

/**
 * Date:2025/01/29 21:13:13
 * Author：Tobin
 * Description:
 */

@Service
public class TestService {

    @DistributedLocker(lockOnParameter = "id")
    public ResponseBean lockerTest(String id) {
        Console.log(id);

        return ResponseBean.success();


    }
}
