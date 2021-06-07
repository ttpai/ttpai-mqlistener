package cn.ttpai.mqlistener.admin.mq.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

import java.util.Date;

/**
 * MQ修复消息VO
 *
 * @author jiayuan.su
 */
public class MqFixMsgVO extends BaseModel {
    /**
     * 无意义主键ID
     */
    private Long id;
    /**
     * 修复ID
     */
    private Long fixId;
    /**
     * 消息文本ID
     */
    private Long msgTextId;
    /**
     * 排序数字
     */
    private Integer orderNum;
    /**
     * 修复状态: 0-待修复, 1-修复中, 2-修复成功, 3-修复异常
     */
    private Integer fixMsgStatus;
    /**
     * 修复备注
     */
    private String fixRemark;
    /**
     * 是否删除了: 0-未删除, 1-已删除
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    /** ----- 以上PO ------------------------------------------------------------ */

    /**
     * 消息文本
     */
    private String msgText;
    /**
     * 添加类型
     *
     * {@link cn.ttpai.mqlistener.admin.mq.enums.MQAddFixMsgTypeEnum}
     */
    private Integer addFixMsgType;
    /**
     * 消息文本列表字符串
     * 说明: ##隔开
     */
    private String msgTextListStr;
    /**
     * 服务器类型
     *
     * {@link cn.ttpai.mqlistener.admin.rabbit.enums.RabbitServerTypeEnum}
     */
    private Integer serverType;
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
    /**
     * 批量保存时的逗号
     */
    private String comma = ",";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFixId() {
        return fixId;
    }

    public void setFixId(Long fixId) {
        this.fixId = fixId;
    }

    public Long getMsgTextId() {
        return msgTextId;
    }

    public void setMsgTextId(Long msgTextId) {
        this.msgTextId = msgTextId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getFixMsgStatus() {
        return fixMsgStatus;
    }

    public void setFixMsgStatus(Integer fixMsgStatus) {
        this.fixMsgStatus = fixMsgStatus;
    }

    public String getFixRemark() {
        return fixRemark;
    }

    public void setFixRemark(String fixRemark) {
        this.fixRemark = fixRemark;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Integer getAddFixMsgType() {
        return addFixMsgType;
    }

    public void setAddFixMsgType(Integer addFixMsgType) {
        this.addFixMsgType = addFixMsgType;
    }

    public String getMsgTextListStr() {
        return msgTextListStr;
    }

    public void setMsgTextListStr(String msgTextListStr) {
        this.msgTextListStr = msgTextListStr;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

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

    public String getComma() {
        return comma;
    }

    public void setComma(String comma) {
        this.comma = comma;
    }
}
