package cn.ttpai.mqlistener.admin.mq.enums;

import java.util.Objects;

/**
 * MQ添加修复消息类型Enum
 *
 * @author jiayuan.su
 */
public enum MQAddFixMsgTypeEnum {
    /**
     * 界面输入
     */
    UI_INPUT(0, "界面输入"),
    /**
     * 文本内容
     */
    TEXT_CONTENT(1, "文本内容"),
    /**
     * 远程获取
     */
    REMOTE_GET(2, "远程获取");

    private Integer type;
    private String text;

    MQAddFixMsgTypeEnum(Integer type, String text) {
        this.type = type;
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public static MQAddFixMsgTypeEnum getEnum(Integer type) {
        if (Objects.isNull(type)) {
            return null;
        }

        for (MQAddFixMsgTypeEnum tempEnum : MQAddFixMsgTypeEnum.values()) {
            if (Objects.equals(tempEnum.getType(), type)) {
                return tempEnum;
            }
        }
        return null;
    }
}
