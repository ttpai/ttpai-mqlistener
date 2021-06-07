package cn.ttpai.mqlistener.admin.zoo.model;

import cn.ttpai.mqlistener.admin.zoo.enums.ProcessTypeEnum;
import cn.ttpai.mqlistener.admin.zoo.enums.ReturnHeadEnum;

import java.io.Serializable;

/**
 * @author zhaopeng
 *
 */
public class ListenerConfig implements Serializable {
	/**
     * 异常队列的名字,virutailHost与原队列一致
     */
    private String exceptionQueue;
    /**
     * 是否放回队列头部
	 * {@link ReturnHeadEnum}
     */
    private Boolean isReturnHead;
    /**
     * 并发消费数
     */
    private Integer concurrentConsumers;
	/**
	 * 消费异常时再处理类型
	 *
	 * @see ProcessTypeEnum
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
	 * 说明: 为null无间隔, 其他有间隔
	 */
	private Long consumeIntervalMs;

	public String getExceptionQueue() {
		return exceptionQueue;
	}

	public void setExceptionQueue(String exceptionQueue) {
		this.exceptionQueue = exceptionQueue;
	}

	public Boolean getIsReturnHead() {
		return isReturnHead;
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
