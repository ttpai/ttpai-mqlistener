package cn.ttpai.mqlistener.admin.zoo.enums;

import java.util.Objects;

/**
 * 消息放回头部
 *
 * @author jiayuan.su
 */
public enum ReturnHeadEnum {
    /**
     * 不放回头部
     */
    NO(0, "不放回头部"),
    /**
     * 放回头部
     */
    YES(1, "放回头部");

    private Integer code;
    private String remark;

    private ReturnHeadEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public static ReturnHeadEnum getEnum(Integer code) {
        for (ReturnHeadEnum tempEnum : ReturnHeadEnum.values()) {
            if (Objects.equals(code, tempEnum.getCode())) {
                return tempEnum;
            }
        }
        return null;
    }

    public static boolean isExist(Integer code) {
        return null != getEnum(code);
    }
}
