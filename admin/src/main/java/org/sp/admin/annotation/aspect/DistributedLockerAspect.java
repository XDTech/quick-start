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

        try {
            boolean gotLocker = rLock.tryLock(); // 加锁失败返回false

            if (!gotLocker) return ResponseBean.fail("请等会再试！");
            joinPoint.proceed();

            return ResponseBean.success();
        } catch (RuntimeException e) {
            // e.printStackTrace();
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
