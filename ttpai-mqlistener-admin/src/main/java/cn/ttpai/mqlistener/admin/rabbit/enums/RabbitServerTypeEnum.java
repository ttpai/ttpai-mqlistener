package cn.ttpai.mqlistener.admin.rabbit.enums;

import java.util.Objects;

/**
 * Rabbit服务器类型Enum
 *
 * @author jiayuan.su
 */
public enum RabbitServerTypeEnum {
    /**
     * APP RabbitMQ
     */
    APP(0, "APP RabbitMQ"),
    /**
     * SOA RabbitMQ
     */
    SOA(1, "SOA RabbitMQ");

    private Integer type;
    private String text;

    RabbitServerTypeEnum(Integer type, String text) {
        this.type = type;
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public static RabbitServerTypeEnum getEnum(Integer type) {
        if (Objects.isNull(type)) {
            return null;
        }

        for (RabbitServerTypeEnum tempEnum : RabbitServerTypeEnum.values()) {
            if (Objects.equals(tempEnum.getType(), type)) {
                return tempEnum;
            }
        }
        return null;
    }

    public static boolean isExist(Integer type) {
        return Objects.nonNull(getEnum(type));
    }

    public static boolean isNotExist(Integer type) {
        return Objects.isNull(getEnum(type));
    }
}
