package cn.ttpai.mqlistener.admin.rabbit.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;
import cn.ttpai.mqlistener.admin.rabbit.enums.RabbitServerTypeEnum;

/**
 * Rabbit发送数据VO
 *
 * @author jiayuan.su
 */
public class RabbitSendDataVO extends BaseModel {
    /**
     * Virtual host
     */
    private String vhost;
    /**
     * 队列名
     */
    private String queueName;
    /**
     * 消息文本
     */
    private String msgText;

    /** ----- 以上与发送数据有关 ---------------------------------------------------- */

    /**
     * 服务器类型
     *
     * {@link RabbitServerTypeEnum}
     */
    private Integer serverType;
    /**
     * 修复消息ID
     */
    private Long fixMsgId;
    /**
     * 是否批量
     * 说明: 1-是, 0-否
     */
    private Integer batched;

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public Long getFixMsgId() {
        return fixMsgId;
    }

    public void setFixMsgId(Long fixMsgId) {
        this.fixMsgId = fixMsgId;
    }

    public Integer getBatched() {
        return batched;
    }

    public void setBatched(Integer batched) {
        this.batched = batched;
    }
}
