package cn.ttpai.mqlistener.core.enums;

import java.util.Objects;

/**
 * 环境类型枚举
 *
 * @author jiayuan.su
 */
public enum EnvTypeEnum {
    /**
     * 开发环境
     */
    DEV("0", "DEV", "开发环境"),
    /**
     * 测试环境
     */
    TEST("1", "TEST", "测试环境"),
    /**
     * 模拟环境
     */
    MONI("2", "MONI", "模拟环境"),
    /**
     * 正式环境
     */
    ONLINE("3", "ONLINE", "正式环境");

    private String code;

    private String symbol;

    private String remark;

    private EnvTypeEnum(String code, String symbol, String remark) {
        this.code = code;
        this.symbol = symbol;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRemark() {
        return remark;
    }

    public static EnvTypeEnum getEnum(String code) {
        for (EnvTypeEnum tempEnum : EnvTypeEnum.values()) {
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
