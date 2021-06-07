package cn.ttpai.mqlistener.admin.mq.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

/**
 * MQ修复消息VO
 *
 * @author jiayuan.su
 */
public class MqFixMsgTextVO extends BaseModel {
    /**
     * 无意义主键ID
     */
    private Long id;
    /**
     * 消息文本
     */
    private String msgText;

    /** ----- 以上PO -------------------------------------- */

    private String comma = ",";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getComma() {
        return comma;
    }

    public void setComma(String comma) {
        this.comma = comma;
    }
}
