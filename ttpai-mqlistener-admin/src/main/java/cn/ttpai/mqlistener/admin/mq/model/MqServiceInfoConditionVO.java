package cn.ttpai.mqlistener.admin.mq.model;

/**
 * MQ服务信息条件VO
 *
 * {@link MqServiceInfoVO}
 *
 * @author jiayuan.su
 */
public class MqServiceInfoConditionVO extends MqServiceInfoVO {
    /**
     * 服务名
     */
    private String serviceNameLike;
    /**
     * 是否清除组信息: 1-是, 0-否
     */
    private Integer clearGroup;

    public String getServiceNameLike() {
        return serviceNameLike;
    }

    public void setServiceNameLike(String serviceNameLike) {
        this.serviceNameLike = serviceNameLike;
    }

    public Integer getClearGroup() {
        return clearGroup;
    }

    public void setClearGroup(Integer clearGroup) {
        this.clearGroup = clearGroup;
    }
}
