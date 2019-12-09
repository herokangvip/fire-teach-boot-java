package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@Component
public class MyEnvironmentPostProcessor  implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try(InputStream input = new FileInputStream("E:\\ds.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("ve", properties);
            environment.getPropertySources().addLast(propertySource);
            System.out.println("====加载外部配置文件完毕====");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

