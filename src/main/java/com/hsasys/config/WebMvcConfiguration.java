package com.hsasys.config;


import com.hsasys.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer
{

    @Value("${swagger.enable}")
    Boolean swaggerEnabled;

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**", "/student/**", "/tutor/**")
                .excludePathPatterns("/user/login", "/user/register");
    }

}
