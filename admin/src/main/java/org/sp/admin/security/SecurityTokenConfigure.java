package org.sp.admin.security;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SecurityTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器 interceptor是拦截器, 是晚于过滤器执行的, satoken的执行是在过滤器阶段 , 所以要配置跨越也必须在过滤器里面配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**").excludePathPatterns("/login/user").excludePathPatterns("/websocket/**"); //除了登录接口，其他接口都需要登录
    }


    /**
     * 注册 [Sa-Token全局过滤器] 不然会报跨域错误 即使配置了跨域
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 拦截与排除 path
                .addInclude("/**").addExclude("/favicon.ico")

                // 全局认证函数
                .setAuth(obj -> {
                    // ...
                })

                // 异常处理函数
                .setError(e -> {
                    System.out.println("---------- 进入Sa-Token异常处理 -----------");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
//                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                });
    }


}
