package com.example.demo.config.web;

import com.example.demo.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
public class MvcConfigurer implements WebMvcConfigurer {
    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String seqId = request.getParameter("seqId");
                if (seqId != null && !"".equals(seqId)) {
                    MDC.put("seqId", seqId);
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                //MDC.remove("seqId");
            }
        });
    }
/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态资源放行
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
                log.error("errorResult,error:", e);

                ModelAndView mv = new ModelAndView();
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                mv.setView(view);
                mv.addObject("status", "fail");
                mv.addObject("message", "系统错误");
                return mv;
            }
        });
    }*/
}
