package cn.ttpai.mqlistener.core;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * MQListener规范
 *
 * @author jiayuan.su
 */
public interface MqListener<T> extends ChannelAwareMessageListener {

    Long errorSleep(int errorCount);

    void onErrorHandler(Exception e, Message message, Channel channel);

    T convertMessage(String json) throws Exception;

    void listener(T e) throws Exception;

    String messageBody(Message message) throws Exception;

    ConnectionFactory getConnectionFactory();

    void setConnectionFactory(ConnectionFactory connectionFactory);

    String getQueue();

    void setQueue(String queue);

    void setBeanId(String beanId);
}
