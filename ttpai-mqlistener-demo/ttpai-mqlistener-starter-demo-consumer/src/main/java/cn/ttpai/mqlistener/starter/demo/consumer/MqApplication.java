package cn.ttpai.mqlistener.starter.demo.consumer;

import cn.ttpai.mqlistener.starter.EnableMqAppServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableMqAppServer
@PropertySources(value = {
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:mqlistener.properties"),
        @PropertySource("classpath:rabbitmq.properties"),
})
public class MqApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MqApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MqApplication.class);
    }
}
