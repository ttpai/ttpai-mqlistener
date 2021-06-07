package cn.ttpai.mqlistener.core;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import cn.ttpai.mqlistener.core.constant.ContainerConstant;
import cn.ttpai.mqlistener.core.email.EmailClient;
import cn.ttpai.mqlistener.core.email.EmailInfo;
import cn.ttpai.mqlistener.core.enums.EnvTypeEnum;
import cn.ttpai.mqlistener.core.enums.ProcessTypeEnum;
import cn.ttpai.mqlistener.core.exception.MqBaseException;
import cn.ttpai.mqlistener.core.util.AssistUtil;
import cn.ttpai.mqlistener.core.util.ContextUtil;
import cn.ttpai.mqlistener.core.zk.ZooManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 抽象listener
 *
 * @author jiayuan.su
 */
public abstract class AbstractListener<T> implements MqListener<T>, BeanNameAware {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * html换行
     */
    private static final String HTML_BR = "<br/><br/>";

    /**
     * 邮件发送间隔时间
     * 30分钟
     */
    private static final long SEND_MAIL_INTERVAL = 30 * 60 * 1000L;

    /**
     * 最后发送时间
     */
    private long lastSendMailTime = 0L;

    private final AtomicInteger errorCount = new AtomicInteger(0);

    @Autowired
    private ZooManager zooManager;

    @Autowired
    private BaseConfig baseConfig;

    @Autowired
    private EmailClient listenerEmailClient;

    /**
     * queue
     */
    private String queue;

    /**
     * spring bean id
     */
    private String beanId;

    /**
     * rabbitmq connection factory
     */
    private ConnectionFactory connectionFactory;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 处理消息，内部使用
     */
    @Override
    public final void onMessage(Message message, Channel channel) throws Exception {
        String msgStr = null;
        try {
            msgStr = this.messageBody(message);
            if (AssistUtil.isBlank(msgStr)) {
                throw new MqBaseException("message_blank", "消息为空");
            }

            T obj = this.convertMessage(msgStr);
            this.listener(obj);

            this.errorCount.set(0);

            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("消息消费失败. 消息内容: {}. 异常详情: ", msgStr, e);
            onErrorHandler(e, message, channel);
        }
        // 处理消费间隔时间
        handleComsumeIntervalTime();
    }

    /**
     * 异常处理,不抛出异常就是ack 1.优先发送异常队列，如果没有异常队列则判断是否方回队列头部还是尾部。 2.记录异常和数据到redis 3.异常信息发邮件 4.根据失败次数睡眠时长
     */
    @Override
    public final void onErrorHandler(Exception ex, Message message, Channel channel) {
        // 1.异常信息
        StringBuilder errorStrBuilder = new StringBuilder();
        errorStrBuilder.append(ExceptionUtils.getFullStackTrace(ex));

        // 2.处理消费异常消息
        processExceptionMsg(message, channel, errorStrBuilder);

        // 3.消息报警
        String msgStr = getMessageStr(message);
        String errorStr = errorStrBuilder.toString();
        // 保存错误信息到zk中
        saveError2Zk(msgStr, errorStr);
        // 发送邮件
        sendMail(msgStr, errorStr);

        // 4.消息消费失败休眠
        sleepForError();
    }

    /**
     * 获取message.body
     */
    @Override
    public String messageBody(Message message) throws Exception {
        return null != message.getBody() && message.getBody().length > 0
                ? new String(message.getBody(), StandardCharsets.UTF_8)
                : null;
    }

    /**
     * JSON转换方法
     */
    @Override
    public T convertMessage(String json) throws Exception {
        return JSON.parseObject(json, getType());
    }

    /**
     * BeanNameAware
     */
    @Override
    public final void setBeanName(String name) {
        setBeanId(name);
    }

    @Override
    public final void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    @Override
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * 注入连接工厂
     */
    @Override
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public String getQueue() {
        return queue;
    }

    /**
     * 注入队列
     */
    @Override
    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
     * 获取泛型类型
     */
    private Type getType() {
        return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 处理异常主流程
     */
    private void processExceptionMsg(Message message, Channel channel, StringBuilder errorStrBuilder) {
        try {
            // 1.deliveryTag及properties
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            AMQP.BasicProperties properties = cloneAmqpProperties(message.getMessageProperties());

            // 2.该bean对应的配置
            final ListenerConfig listenerConfig = ContextUtil.getListenerConfig(this.beanId);
            if (null == listenerConfig) {
                logger.error("warn：{}对应listenerConfig不正确", this.beanId);
                // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
                return;
            }

            // 3.老版本数据处理
            if (null == listenerConfig.getProcessType()) {
                processExceptionMsg4OldVersion(message, channel, listenerConfig, deliveryTag, properties);
                return;
            }
            // 4.新版本数据处理
            processExceptionMsg4NewVersion(message, channel, listenerConfig, deliveryTag, properties);
        } catch (Exception e) {
            logger.error("exceptionQueue", e);
            errorStrBuilder.append(HTML_BR).append(ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 克隆属性 spring messageProperties to rabbmit messageProperties
     */
    private AMQP.BasicProperties cloneAmqpProperties(MessageProperties messageProperties) {
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
        BeanUtils.copyProperties(messageProperties, basicProperties);

        try {
            if (null == basicProperties.getHeaders()) {
                Field headersField = AMQP.BasicProperties.class.getDeclaredField("headers");
                headersField.setAccessible(true);
                headersField.set(basicProperties, new HashMap<String, Object>());
            }
        } catch (Exception e) {
            logger.error("反射异常，请检查");
            throw new MqBaseException("system_error", "反射异常，请检查");
        }

        if (null != messageProperties.getHeaders()) {
            basicProperties.getHeaders().putAll(messageProperties.getHeaders());
        }

        return basicProperties;
    }

    /**
     * 处理异常消息之老版本兼容
     */
    private void processExceptionMsg4OldVersion(Message message, Channel channel, ListenerConfig listenerConfig,
                                                long deliveryTag, AMQP.BasicProperties properties) throws IOException {
        if (AssistUtil.isNotBlank(listenerConfig.getExceptionQueue())) {
            // 如果有异常队列，优先发送。
            putMsg2ExceptionQueue(message, channel, deliveryTag, properties, listenerConfig.getExceptionQueue());
        } else if (Objects.equals(listenerConfig.getIsReturnHead(), true)) {
            // 拒绝，重新消费
            reputMsg2QueueHead(channel, deliveryTag);
        } else if (Objects.equals(listenerConfig.getIsReturnHead(), false)) {
            reputMsg2QueueEnd(message, channel, deliveryTag, properties);
        } else {
            logger.error("warn：{}对应listenerConfig不正确", this.beanId);
            // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
        }
    }

    /**
     * 处理异常消息之新版本
     */
    private void processExceptionMsg4NewVersion(Message message, Channel channel, ListenerConfig listenerConfig,
                                                long deliveryTag, AMQP.BasicProperties properties) throws IOException {
        ProcessTypeEnum processTypeEnum = ProcessTypeEnum.getEnum(listenerConfig.getProcessType());
        switch (processTypeEnum) {
            case EXCEPTION_QUEUE:
                processExceptionMsg4ExceptionQueue(message, channel, deliveryTag, properties, listenerConfig);
                break;
            case REPUT_QUEUE:
                processExceptionMsg4ReputQueue(message, channel, deliveryTag, properties, listenerConfig);
                break;
            case REPUT_QUEUE_COUNT_DISCARD:
                processExceptionMsg4ReputQueueCountDiscard(message, channel, deliveryTag, properties, listenerConfig);
                break;
            case REPUT_QUEUE_COUNT_EXCEPTION_QUEUE:
                processExceptionMsg4ReputQueueCountExceptionQueue(message, channel, deliveryTag, properties,
                        listenerConfig);
                break;
            default:
                logger.error("warn：{}的处理类型{}不正确", this.beanId, processTypeEnum.getCode());
                // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
        }
    }

    /**
     * 处理异常消息之类型为 EXCEPTION_QUEUE 的新版本
     */
    private void processExceptionMsg4ExceptionQueue(Message message, Channel channel, long deliveryTag,
                                                    AMQP.BasicProperties properties,
                                                    ListenerConfig listenerConfig) throws IOException {
        String exceptionQueue = listenerConfig.getExceptionQueue();

        if (StringUtils.isBlank(exceptionQueue)) {
            logger.error("warn：{}的处理类型为 EXCEPTION_QUEUE 的配置不正确", this.beanId);
            // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
            return;
        }
        putMsg2ExceptionQueue(message, channel, deliveryTag, properties, exceptionQueue);
    }

    /**
     * 处理异常消息之类型为REPUT_QUEUE的新版本
     */
    private void processExceptionMsg4ReputQueue(Message message, Channel channel, long deliveryTag,
                                                AMQP.BasicProperties properties,
                                                ListenerConfig listenerConfig) throws IOException {
        Boolean isReturnHead = listenerConfig.getIsReturnHead();

        if (null == isReturnHead) {
            logger.error("warn：{}的处理类型为 EXCEPTION_QUEUE 的配置不正确", this.beanId);
            // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
            return;
        }

        if (Objects.equals(isReturnHead, true)) {
            reputMsg2QueueHead(channel, deliveryTag);
            return;
        }
        reputMsg2QueueEnd(message, channel, deliveryTag, properties);
    }

    /**
     * 处理异常消息之类型为 REPUT_QUEUE_COUNT_DISCARD 的新版本
     */
    private void processExceptionMsg4ReputQueueCountDiscard(Message message, Channel channel, long deliveryTag,
                                                            AMQP.BasicProperties properties,
                                                            ListenerConfig listenerConfig) throws IOException {
        Integer failLimit = listenerConfig.getFailLimit();

        // 1.数据不正确
        if (null == failLimit || failLimit < 1) {
            logger.error("warn：{}的处理类型为 REPUT_QUEUE_COUNT_DISCARD 的配置不正确", this.beanId);
            // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
            return;
        }

        // 2.消费失败1次时直接返回，即直接丢弃消息
        if (1 == failLimit) {
            logger.debug("消费失败1次丢弃消息, 消息体: {}", getMessageStr(message));
            discardMsg(channel, deliveryTag);
            return;
        }

        // 3.获取消费失败次数
        Integer failCount = getFailCount(properties);

        // 4.消费失败次数达到限制，丢弃消息
        if (failCount >= failLimit) {
            logger.debug("消费失败{}次丢弃消息, 消息体: {}", failCount, getMessageStr(message));
            discardMsg(channel, deliveryTag);
            return;
        }

        // 5.消费失败次数未达到限制
        Map<String, Object> msgHeaderMap = properties.getHeaders();
        if (null != msgHeaderMap) {
            // 克隆处null时已赋了空map了
            msgHeaderMap.put(ContainerConstant.Mq.MSG_CONSUMED_FAIL_COUNT_KEY, failCount);
        }
        reputMsg2QueueEnd(message, channel, deliveryTag, properties);
    }

    /**
     * 处理异常消息之类型为 REPUT_QUEUE_COUNT_EXCEPTION_QUEUE 的新版本
     */
    private void processExceptionMsg4ReputQueueCountExceptionQueue(Message message, Channel channel, long deliveryTag,
                                                                   AMQP.BasicProperties properties,
                                                                   ListenerConfig listenerConfig) throws IOException {
        final Integer failLimit = listenerConfig.getFailLimit();
        final String exceptionQueue = listenerConfig.getExceptionQueue();

        // 1.数据不正确
        if (null == failLimit || failLimit < 1 || StringUtils.isBlank(exceptionQueue)) {
            logger.error("warn：{}的处理类型为 REPUT_QUEUE_COUNT_EXCEPTION_QUEUE 的配置不正确", this.beanId);
            // 配置不完整时(基本不可能不完整)，不回应消息，即消息一直不能消费，此时需人工确认
            return;
        }

        // 2.消费失败1次时把消息放入异常队列
        if (1 == failLimit) {
            logger.debug("消费失败1次放入异常队列, 消息体: {}", getMessageStr(message));
            putMsg2ExceptionQueue(message, channel, deliveryTag, properties, exceptionQueue);
            return;
        }

        // 3.获取消费失败次数
        Integer failCount = getFailCount(properties);

        // 4.消费失败次数达到限制，放入异常队列
        if (failCount >= failLimit) {
            logger.debug("消费失败{}次放入异常队列, 消息体: {}", failCount, getMessageStr(message));
            putMsg2ExceptionQueue(message, channel, deliveryTag, properties, exceptionQueue);
            return;
        }

        // 5.消费失败次数未达到限制
        Map<String, Object> msgHeaderMap = properties.getHeaders();
        if (null != msgHeaderMap) {
            msgHeaderMap.put(ContainerConstant.Mq.MSG_CONSUMED_FAIL_COUNT_KEY, failCount);
        }
        reputMsg2QueueEnd(message, channel, deliveryTag, properties);
    }

    /**
     * 把消息放入异常队列
     */
    private void putMsg2ExceptionQueue(Message message, Channel channel, long deliveryTag,
                                       AMQP.BasicProperties properties, String exceptionQueue) throws IOException {
        channel.basicReject(deliveryTag, false);
        channel.basicPublish("", exceptionQueue, false, false, properties, message.getBody());
    }

    /**
     * 丢弃消息
     */
    private void discardMsg(Channel channel, long deliveryTag) throws IOException {
        channel.basicReject(deliveryTag, false);
    }

    /**
     * 把消息放回队列头部
     */
    private void reputMsg2QueueHead(Channel channel, long deliveryTag) throws IOException {
        channel.basicReject(deliveryTag, true);
    }

    /**
     * 把消息放回队列尾部
     */
    private void reputMsg2QueueEnd(Message message, Channel channel, long deliveryTag,
                                   AMQP.BasicProperties properties) throws IOException {
        channel.basicReject(deliveryTag, false);
        channel.basicPublish("", queue, false, false, properties, message.getBody());
    }

    /**
     * 获取失败次数(header中的+1)
     */
    private Integer getFailCount(AMQP.BasicProperties properties) {
        Integer failCount = null;

        Map<String, Object> msgHeaderMap = properties.getHeaders();
        if (null != msgHeaderMap) {
            failCount = (Integer) msgHeaderMap.get(ContainerConstant.Mq.MSG_CONSUMED_FAIL_COUNT_KEY);
        }
        if (null == failCount) {
            failCount = 0;
        }

        return ++failCount;
    }

    /**
     * 获取消息字符串
     */
    private String getMessageStr(Message message) {
        String msgStr = null;
        try {
            msgStr = this.messageBody(message);
        } catch (Exception e) {
            logger.error("warn：消息转换异常", e);
            msgStr = "(消息转换异常)";
        }
        return msgStr;
    }

    /**
     * 保存错误信息到zk中
     */
    private void saveError2Zk(String msgStr, String errorStr) {
        try {
            final ContainerInfo containerInfo = ContextUtil.getContainerInfo();

            String listenerPath = AssistUtil.getListenerPath(containerInfo.getProjectName(), this.beanId);
            String instancePath = AssistUtil.getListenerInstancePath(listenerPath, containerInfo.getIpPort());
            String instanceErrorPath = AssistUtil.getInstanceErrorPath(instancePath);

            String errorDesc = AssistUtil.getErrorDesc(msgStr, AssistUtil.getCurDateStr(), errorStr);
            if (this.zooManager.isZooPathExist(instanceErrorPath)) {
                this.zooManager.setData2Zoo(instanceErrorPath, errorDesc);
            } else {
                logger.error("warn：{}对应实例错误节点不存在", this.beanId);
            }
        } catch (Exception e) {
            logger.error("保存错误信息到zk中异常", e);
        }
    }

    /**
     * 发送邮件,每间隔半小时发一次报警
     */
    private void sendMail(String msgStr, String errorStr) {
        lock.lock();
        try {

            // 1.检查时间间隔
            long now = System.currentTimeMillis();
            if (0L != lastSendMailTime && (now - lastSendMailTime) < SEND_MAIL_INTERVAL) {
                return;
            }

            // 2.发送邮件
            String projectNameText = "<p><b>项目:</b></p>" + this.baseConfig.getProjectName();
            String listenerNameText = "<p><b>监听器:</b></p>" + this.beanId;
            String queueText = "<p><b>队列:</b></p>" + this.queue;
            String msgText = "<p><b>消息:</b></p>" + msgStr;
            String errorText = "<p><b>错误详情:</b></p>" + errorStr;

            String txtBody = projectNameText + HTML_BR
                    + listenerNameText + HTML_BR
                    + queueText + HTML_BR
                    + msgText + HTML_BR
                    + errorText;

            EmailInfo emailInfo = new EmailInfo();
            emailInfo.setReceivers(AssistUtil.getReceiverArray(baseConfig.getReceivers()));
            emailInfo.setSubject("【" + getEnvText(baseConfig.getEnvCode()) + "】"
                    + "【MQListener告警】"
                    + "【" + this.baseConfig.getProjectName() + "】"
                    + "【" + this.queue + "】");
            emailInfo.setContent(txtBody);
            listenerEmailClient.sendEmail(emailInfo);

            // 3.记录最后发送时间
            lastSendMailTime = System.currentTimeMillis();
        } catch (Exception e) {
            logger.error("send mail exception", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取环境文字
     */
    private String getEnvText(String envCode) {
        EnvTypeEnum envTypeEnum = EnvTypeEnum.getEnum(envCode);
        return Objects.isNull(envTypeEnum) ? "未知环境" : envTypeEnum.getRemark();
    }

    /**
     * 消息消费失败休眠
     */
    private void sleepForError() {
        try {
            int num = this.errorCount.incrementAndGet();
            TimeUnit.MILLISECONDS.sleep(errorSleep(num));
        } catch (InterruptedException ie) {
            logger.error("onMessage error sleep", ie);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 根据错误次数设置睡眠时长
     */
    @Override
    public Long errorSleep(int errorCount) {
        if (errorCount < 10) {
            return 1000L;
        }
        if (errorCount < 20) {
            return 2000L;
        }
        if (errorCount < 50) {
            return 5000L;
        }
        return 10000L;
    }

    /**
     * 处理消费间隔时间
     */
    private void handleComsumeIntervalTime() {
        final ListenerConfig listenerConfig = ContextUtil.getListenerConfig(this.beanId);
        if (null == listenerConfig) {
            logger.error("warn：{}对应listenerConfig不正确", this.beanId);
            // 配置不完整时不处理
            return;
        }

        Long consumeIntervalMs = listenerConfig.getConsumeIntervalMs();
        if (Objects.nonNull(consumeIntervalMs) && consumeIntervalMs > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(consumeIntervalMs);
            } catch (InterruptedException ie) {
                logger.error("onMessage sleep", ie);
                Thread.currentThread().interrupt();
            }
        }
    }
}
