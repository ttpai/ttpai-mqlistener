package cn.ttpai.mqlistener.admin.mq.enums;

import java.util.Objects;

/**
 * MQ修复消息状态Enum
 *
 * @author jiayuan.su
 */
public enum MQFixMsgStatusEnum {
    /**
     * 待修复
     */
    WAIT_FIX(0, "待修复"),
    /**
     * 修复中
     */
    IN_FIX(1, "修复中"),
    /**
     * 修复成功
     */
    FIX_SUCCESS(2, "修复成功"),
    /**
     * 修复异常
     */
    FIX_ERROR(3, "修复异常");

    private Integer status;
    private String text;

    MQFixMsgStatusEnum(Integer status, String text) {
        this.status = status;
        this.text = text;
    }

    public Integer getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public static MQFixMsgStatusEnum getEnum(Integer status) {
        if (Objects.isNull(status)) {
            return null;
        }
        for (MQFixMsgStatusEnum tempEnum : MQFixMsgStatusEnum.values()) {
            if (Objects.equals(tempEnum.getStatus(), status)) {
                return tempEnum;
            }
        }
        return null;
    }

    public static boolean isExist(Integer status) {
        return Objects.nonNull(getEnum(status));
    }

    public static boolean isNotExist(Integer status) {
        return Objects.isNull(getEnum(status));
    }
}
