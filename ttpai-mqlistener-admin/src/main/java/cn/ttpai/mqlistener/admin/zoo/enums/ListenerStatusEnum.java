package cn.ttpai.mqlistener.admin.zoo.enums;

import java.util.Objects;

/**
 * @Auther: wangtq
 * @Date: 2019/2/20 14:18
 * @Description:mq节点是否启用
 */
public enum ListenerStatusEnum {
    /**
     * 停用
     */
    STOP("0","停用"),
    /**
     * 启用
     */
    START("1","启用");

    private String code;
    private String value;

    private ListenerStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ListenerStatusEnum getEnum(String code) {
        for (ListenerStatusEnum tempEnum : ListenerStatusEnum.values()) {
            if (Objects.equals(code, tempEnum.getCode())) {
                return tempEnum;
            }
        }
        return null;
    }

    public static boolean isExist(String code) {
        return null != getEnum(code);
    }
}
