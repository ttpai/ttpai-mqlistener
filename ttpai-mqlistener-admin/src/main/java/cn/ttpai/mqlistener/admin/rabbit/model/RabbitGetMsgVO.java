package cn.ttpai.mqlistener.admin.rabbit.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

/**
 * RabbitMQ获取消息VO
 *
 * @author jiayuan.su
 */
public class RabbitGetMsgVO extends BaseModel {
    /**
     * virtual host
     */
    private String vhost;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 获取数量
     */
    private Integer getNum;

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

    public Integer getGetNum() {
        return getNum;
    }

    public void setGetNum(Integer getNum) {
        this.getNum = getNum;
    }
}
