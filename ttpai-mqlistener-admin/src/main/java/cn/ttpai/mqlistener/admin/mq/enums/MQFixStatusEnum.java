package cn.ttpai.mqlistener.admin.mq.enums;

import java.util.Objects;

/**
 * MQ修复状态Enum
 *
 * @author jiayuan.su
 */
public enum MQFixStatusEnum {
    /**
     * 修复中
     */
    IN_FIX(0, "修复中"),
    /**
     * 已结束
     */
    FIX_END(1, "已结束");

    private Integer status;
    private String text;

    MQFixStatusEnum(Integer status, String text) {
        this.status = status;
        this.text = text;
    }

    public Integer getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    /**
      * 根据修复状态获取枚举
      *
      * @param status 修复状态
      * @return MQFixStatusEnum
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public static MQFixStatusEnum getEnum(Integer status) {
        if (Objects.isNull(status)) {
            return null;
        }

        for (MQFixStatusEnum fixStatusEnum : MQFixStatusEnum.values()) {
            if (Objects.equals(fixStatusEnum.getStatus(), status)) {
                return fixStatusEnum;
            }
        }
        return null;
    }

    /**
      * 根据修复状态判断是否存在对应枚举
      *
      * @param status 修复状态
      * @return boolean true-存在, false-不存在
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public static boolean isExist(Integer status) {
        return Objects.nonNull(getEnum(status));
    }

    /**
     * 根据修复状态判断是否不存在对应枚举
     *
     * @param status 修复状态
     * @return boolean true-不存在, false-存在
     * @author jiayuan.su
     * @date 2020年09月09日
     */
    public static boolean isNotExist(Integer status) {
        return Objects.isNull(getEnum(status));
    }
}
