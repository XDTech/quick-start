package org.sp.admin;

import cn.hutool.core.lang.Console;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.sp.admin.annotation.DistributedLocker;
import org.sp.admin.beans.ResponseBean;
import org.sp.admin.service.TestService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminApplicationTests {


    @Resource
    private TestService testService;

    @Test
    void contextLoads() {
        this.testService.lockerTest("123");
    }


}
