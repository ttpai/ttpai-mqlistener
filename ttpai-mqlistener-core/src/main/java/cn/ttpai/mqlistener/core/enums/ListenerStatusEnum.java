package cn.ttpai.mqlistener.core.enums;

import java.util.Objects;

/**
 * 监听器状态
 *
 * @author jiayuan.su
 */
public enum ListenerStatusEnum {
    /**
     * 暂停
     */
    SUSPENDED("0", "暂停"),
    /**
     * 运行
     */
    RUNNING("1", "运行");

    private String code;

    private String remark;

    private ListenerStatusEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
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
