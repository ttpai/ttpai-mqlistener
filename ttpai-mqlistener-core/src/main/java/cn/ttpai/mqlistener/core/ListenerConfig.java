package cn.ttpai.mqlistener.core;

/**
 * @author zhaopeng
 */
public class ListenerConfig {

    /**
     * 异常队列的名字,virutailHost与原队列一致
     */
    private String exceptionQueue;

    /**
     * 是否放回队列头部
     */
    private Boolean isReturnHead;

    /**
     * 消费者的最大个数，因为在spring中messageListener实例是单例的， spring-jms不能创建多个messageListener实例来并发消费。
     * 所以spring在内部，创建了多个MessageConsumer实例，并使用consumer.receive()方法以阻塞的方式来获取消息，
     * 当获取消息后，在执行messageListener.onMessage()方法； concurrentConsumers属性就是为了指定spring内部可以创建MessageConsumer的最大个数；
     * 当messageConsumer实例被创建后，将会封装在一个Runner接口并交给taskExecutor来调度； 如果consumer在一直没有收到消息，则会被置为“idle”并从consumer列表中移除；
     * 如果所有的consumer都处于active状态，则会创建新的consumer实例直到达到maxConcurrentConsumers个数上限。
     * 通常taskExecutor的线程池容量稍大于concurrentConsumer。
     */
    private Integer concurrentConsumers;

    /**
     * 消费异常时再处理类型
     */
    private Integer processType;

    /**
     * 消费失败次数
     */
    private Integer failLimit;

    /**
     * 预抓取消息数
     */
    private Integer prefetchCount;

    /**
     * 消费间隔毫秒数
     * 说明: 为null时无间隔, 其他有间隔
     */
    private Long consumeIntervalMs;

    public String getExceptionQueue() {
        return exceptionQueue;
    }

    public void setExceptionQueue(String exceptionQueue) {
        this.exceptionQueue = exceptionQueue;
    }

    public Boolean getIsReturnHead() {
        return this.isReturnHead;
    }

    public void setIsReturnHead(Boolean isReturnHead) {
        this.isReturnHead = isReturnHead;
    }

    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public Integer getFailLimit() {
        return failLimit;
    }

    public void setFailLimit(Integer failLimit) {
        this.failLimit = failLimit;
    }

    public Integer getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(Integer prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public Long getConsumeIntervalMs() {
        return consumeIntervalMs;
    }

    public void setConsumeIntervalMs(Long consumeIntervalMs) {
        this.consumeIntervalMs = consumeIntervalMs;
    }
}
