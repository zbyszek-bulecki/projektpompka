package com.sharks.gardenManager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AngularConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/app").setViewName("forward:/app/index.html");
        registry.addViewController("/app/").setViewName("forward:/app/index.html");
        registry.addViewController("/app/{path:^[^.]+$}").setViewName("forward:/app/index.html");
        registry.addViewController("/app/{path:^[^.]+$}/**").setViewName("forward:/app/index.html");
        registry.addRedirectViewController("/", "/app");
    }
}
