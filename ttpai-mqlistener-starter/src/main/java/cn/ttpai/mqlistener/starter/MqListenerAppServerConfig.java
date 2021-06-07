package cn.ttpai.mqlistener.starter;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * mq listener app server 配置
 *
 * @author jiayuan.su
 */
@Import({MqListenerProperties.class})
public class MqListenerAppServerConfig {

    @Autowired
    private MqListenerProperties mqListenerProperties;

    @Bean("abstractConnectionFactory4App")
    @Scope("prototype")
    public ConnectionFactory abstractConnectionFactory4App() {
        ConnectionFactory abstractConnectionFactory4App = new ConnectionFactory();
        abstractConnectionFactory4App.setHost(mqListenerProperties.getRabbitmqAppHost());
        abstractConnectionFactory4App.setPort(mqListenerProperties.getRabbitmqAppPort());
        abstractConnectionFactory4App.setUsername(mqListenerProperties.getRabbitmqAppUsername());
        abstractConnectionFactory4App.setPassword(mqListenerProperties.getRabbitmqAppPassword());
        abstractConnectionFactory4App.setRequestedHeartbeat(60);
        return abstractConnectionFactory4App;
    }
}
