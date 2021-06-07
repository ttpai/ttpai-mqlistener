package cn.ttpai.mqlistener.core.enums;

import java.util.Objects;

public enum ProcessTypeEnum {
    /**
     * 直接放入异常队列
     */
    EXCEPTION_QUEUE(1, "直接放入异常队列"),
    /**
     * 放回队头或队尾
     */
    REPUT_QUEUE(2, "放回队头或队尾)"),
    /**
     * 放回队尾，消费一定次数后丢弃
     */
    REPUT_QUEUE_COUNT_DISCARD(3, "放回队尾，消费一定次数后丢弃"),
    /**
     * 放回队尾，消费一定次数后放入异常队列
     */
    REPUT_QUEUE_COUNT_EXCEPTION_QUEUE(4, "放回队尾，消费一定次数后放入异常队列");

    private Integer code;

    private String remark;

    ProcessTypeEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public static ProcessTypeEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }

        for (ProcessTypeEnum tempEnum : ProcessTypeEnum.values()) {
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
