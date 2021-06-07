package cn.ttpai.mqlistener.starter;

import cn.ttpai.mqlistener.core.BaseConfig;
import cn.ttpai.mqlistener.core.MqListenerContainerClose;
import cn.ttpai.mqlistener.core.MqListenerContainerInit;
import cn.ttpai.mqlistener.core.email.EmailClient;
import cn.ttpai.mqlistener.core.email.EmailConfig;
import cn.ttpai.mqlistener.core.zk.ZooClientFactory;
import cn.ttpai.mqlistener.core.zk.ZooManager;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * mq 基本配置
 *
 * @author jiayuan.su
 */
@Import({MqListenerProperties.class})
public class MqListenerBaseConfig {

    @Autowired
    private MqListenerProperties mqListenerProperties;

    @Bean
    public BaseConfig baseConfig() {
        BaseConfig baseConfig = new BaseConfig();
        baseConfig.setProjectName(mqListenerProperties.getProjectName());
        baseConfig.setEnvCode(mqListenerProperties.getEnvCode());
        baseConfig.setReceivers(mqListenerProperties.getExceptionEmailReceivers());
        return baseConfig;
    }

    @Bean
    public ExponentialBackoffRetry exponentialBackoffRetry() {
        return new ExponentialBackoffRetry(mqListenerProperties.getZooBaseSleepTimeMs(),
                mqListenerProperties.getZooMaxRetries());
    }

    @Bean
    public ZooClientFactory zooClientFactory(@Autowired ExponentialBackoffRetry exponentialBackoffRetry) {
        ZooClientFactory zooClientFactory = new ZooClientFactory();
        zooClientFactory.setConnectionString(mqListenerProperties.getZooAddress());
        zooClientFactory.setRetryPolicy(exponentialBackoffRetry);
        return zooClientFactory;
    }

    @Bean
    public ZooManager zooManager(@Autowired ZooClientFactory zooClientFactory) {
        ZooManager zooManager = new ZooManager();
        zooManager.setZooClient(zooClientFactory.getObject());
        zooManager.setRootPath(mqListenerProperties.getZooRootPath());
        return zooManager;
    }

    @Bean
    public EmailConfig emailConfig() {
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setHostName(mqListenerProperties.getEmailClientHost());
        emailConfig.setSmtpPort(mqListenerProperties.getEmailClientPort());
        emailConfig.setUsername(mqListenerProperties.getEmailClientUsername());
        emailConfig.setPassword(mqListenerProperties.getEmailClientPassword());
        emailConfig.setSslOnConnect(mqListenerProperties.getEmailClientUsessl());
        emailConfig.setFrom(mqListenerProperties.getEmailClientFrom());
        emailConfig.setCharset(mqListenerProperties.getEmailClientCharset());
        return emailConfig;
    }

    @Bean
    public EmailClient emailClient(@Autowired EmailConfig emailConfig) {
        return new EmailClient(emailConfig);
    }

    @Bean
    public MqListenerContainerInit mqListenerContainerInit() {
        return new MqListenerContainerInit();
    }

    @Bean
    public MqListenerContainerClose mqListenerContainerClose() {
        return new MqListenerContainerClose();
    }
}
