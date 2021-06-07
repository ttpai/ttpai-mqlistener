package cn.ttpai.mqlistener.admin.common.enums;

/**
 * yes or no 枚举
 *
 * @author jiayuan.su
 */
public enum YesOrNoEnum {
    /**
     * 否
     */
    NO(0),
    /**
     * 是
     */
    YES(1);

    private Integer code;

    YesOrNoEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
