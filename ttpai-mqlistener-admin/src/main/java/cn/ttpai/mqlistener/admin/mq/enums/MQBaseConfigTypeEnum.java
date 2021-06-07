package cn.ttpai.mqlistener.admin.mq.enums;

/**
 * MQ基础配置类型Enum
 *
 * @author jiayuan.su
 */
public enum MQBaseConfigTypeEnum {
    /**
     * zk到db
     */
    ZK_TO_DB(100, "zk到db");

    private Integer code;

    private String desc;

    MQBaseConfigTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
