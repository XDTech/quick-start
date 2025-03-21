package org.sp.admin.annotation;

import java.lang.annotation.*;

/**
 * Date:2025/01/29 20:30:23
 * Author：Tobin
 * Description: 表单反重复提交 基于Redisson分布式锁 AOP
 */


@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLocker {
    /**
     * 需要加锁的参数，例如创建序列号记录时，序列号即为传入的参数，可以将该参数指定为需要加锁的参数
     *
     * @DistributedLocker(lockOnParameter =
     *                                    "exampleVariable") @GetMapping("/test/{exampleVariable}")
     *                                    public boolean test(@PathVariable String
     *                                    exampleVariable) { ... }
     * @return
     */
    /**
     * 添加支持多参数
     *
     * @return
     */
    String lockOnParameter();
    int maxRetryCount() default 3; // 最大重试次数

    int retryIntervalMs() default 1000; // 重试间隔（毫秒）

    boolean enableRetry() default true;  // 是否启用重试机制

}
