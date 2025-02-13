package org.sp.admin.annotation.aspect;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.sp.admin.annotation.DistributedLocker;
import org.sp.admin.beans.ResponseBean;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Date:2025/01/29 20:33:00
 * Author：Tobin
 * Description: AOP实现类，支持多参数
 */

@Aspect
@Component
public class DistributedLockerAspect {

    @Resource
    private Redisson redisson;

    @Around(value = "@annotation(org.sp.admin.annotation.DistributedLocker)")
    public ResponseBean handleDistributedLock(ProceedingJoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 通过反射获取自定义注解对象
        DistributedLocker distributedLocker = methodSignature.getMethod().getAnnotation(DistributedLocker.class);

        String parameter = distributedLocker.lockOnParameter();
        String value = this.getParameterInString(joinPoint, parameter);

        String className = methodSignature.getDeclaringTypeName();  // 获取目标类的全类名
        String methodName = methodSignature.getName();

        String lockKey = StrUtil.format("{}_{}_{}", className, methodName, value);

        RLock rLock = this.redisson.getLock(lockKey);

        int maxRetryCount = distributedLocker.maxRetryCount();  // 最大重试次数
        int retryIntervalMs = distributedLocker.retryIntervalMs();  // 重试间隔
        boolean enableRetry = distributedLocker.enableRetry();  // 是否启用重试机制

        int attempt = 0;
        boolean gotLock = false;

        try {
            // 如果启用了重试机制，则执行重试逻辑
            if (enableRetry) {
                while (attempt < maxRetryCount) {
                    gotLock = rLock.tryLock(); // 尝试获取锁并设置过期时间

                    if (gotLock) {
                        break;  // 成功获取锁，退出循环
                    }

                    attempt++;  // 增加尝试次数
                    if (attempt < maxRetryCount) {
                        Thread.sleep(retryIntervalMs);  // 等待重试
                    }
                }
            } else {
                // 如果没有启用重试机制，直接尝试一次获取锁
                gotLock = rLock.tryLock();
            }

            if (!gotLock) {
                return ResponseBean.fail("请等会再试！");
            }

            Object proceed = joinPoint.proceed();// 执行目标方法得到返回结果
            return (ResponseBean) proceed;// 转换 返回

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 如果线程被中断，则恢复中断状态
            return ResponseBean.fail("操作中断，请稍后再试！");
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Throwable e) {
            return ResponseBean.fail("请等会再试！");
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    private String getParameterInString(ProceedingJoinPoint joinPoint, String parameterName) {
        Object valueParameter = null;
        if (ObjectUtil.isNotNull(joinPoint) && joinPoint.getSignature() instanceof MethodSignature && ObjectUtil.isNotNull(parameterName)) {
            MethodSignature method = (MethodSignature) joinPoint.getSignature();
            String[] parameters = method.getParameterNames();
            StringBuffer keyBuffer = new StringBuffer();
            String[] keyPartArray = parameterName.split(",");
            for (int i = 0; i < keyPartArray.length; i++) {
                for (int t = 0; t < parameters.length; t++) {
                    if (ObjectUtil.isNotNull(parameters[t]) && parameters[t].equals(keyPartArray[i])) {
                        Object[] obj = joinPoint.getArgs();
                        keyBuffer.append(obj[t]);

                    }
                }
            }
            valueParameter = keyBuffer.toString();
        }

        return String.valueOf(valueParameter);
    }
}
