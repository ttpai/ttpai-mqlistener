package cn.ttpai.mqlistener.starter.demo.consumer.config;

import cn.ttpai.mqlistener.starter.demo.consumer.listener.TestListener;
import cn.ttpai.mqlistener.starter.demo.consumer.listener.TestListener2;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * mq listener 配置
 *
 * @author jiayuan.su
 */
@Configuration
public class MqListenerConfig {

    @Value("${rabbitmq.vhost.test}")
    private String vhostTest;

    @Value("${rabbitmq.queue.test}")
    private String queueTest;

    @Value("${rabbitmq.vhost.test2}")
    private String vhostTest2;

    @Value("${rabbitmq.queue.test2}")
    private String queueTest2;

    @Bean("demoConnectionFactory")
    @Primary
    public ConnectionFactory connectionFactory(@Qualifier("abstractConnectionFactory4App") com.rabbitmq.client.ConnectionFactory abstractConnectionFactory4App) {
        CachingConnectionFactory demoConnectionFactory = new CachingConnectionFactory(abstractConnectionFactory4App);
        demoConnectionFactory.setVirtualHost(vhostTest);
        return demoConnectionFactory;
    }

    @Bean("demo2ConnectionFactory")
    public ConnectionFactory connectionFactory2(@Qualifier("abstractConnectionFactory4App") com.rabbitmq.client.ConnectionFactory abstractConnectionFactory4App) {
        CachingConnectionFactory demoConnectionFactory = new CachingConnectionFactory(abstractConnectionFactory4App);
        demoConnectionFactory.setVirtualHost(vhostTest2);
        return demoConnectionFactory;
    }

    @Bean("testListener")
    public TestListener testListener(@Qualifier("demoConnectionFactory") ConnectionFactory demoConnectionFactory) {
        TestListener testListener = new TestListener();
        testListener.setConnectionFactory(demoConnectionFactory);
        testListener.setQueue(queueTest);
        return testListener;
    }

    @Bean("testListener2")
    public TestListener2 testListener2(@Qualifier("demo2ConnectionFactory") ConnectionFactory demo2ConnectionFactory) {
        TestListener2 testListener = new TestListener2();
        testListener.setConnectionFactory(demo2ConnectionFactory);
        testListener.setQueue(queueTest2);
        return testListener;
    }
}
