package cn.ttpai.mqlistener.starter;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * mq listener soa server 配置
 *
 * @author jiayuan.su
 */
@Import({MqListenerProperties.class})
public class MqListenerSoaServerConfig {

    @Autowired
    private MqListenerProperties mqListenerProperties;

    @Bean("abstractConnectionFactory4Soa")
    @Scope("prototype")
    public ConnectionFactory abstractConnectionFactory4Soa() {
        ConnectionFactory abstractConnectionFactory4Soa = new ConnectionFactory();
        abstractConnectionFactory4Soa.setHost(mqListenerProperties.getRabbitmqSoaHost());
        abstractConnectionFactory4Soa.setPort(mqListenerProperties.getRabbitmqSoaPort());
        abstractConnectionFactory4Soa.setUsername(mqListenerProperties.getRabbitmqSoaUsername());
        abstractConnectionFactory4Soa.setPassword(mqListenerProperties.getRabbitmqSoaPassword());
        abstractConnectionFactory4Soa.setRequestedHeartbeat(60);
        return abstractConnectionFactory4Soa;
    }
}
