package cn.ttpai.mqlistener.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author jiayuan.su
 */
public class MqListenerPropertyConfig {

    @Bean
    @ConfigurationProperties(prefix = "mqlistener")
    public MqListenerProperties mqListenerProperties() {
        return new MqListenerProperties();
    }
}
