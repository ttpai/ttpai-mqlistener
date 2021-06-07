package cn.ttpai.mqlistener.admin.mq.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

import java.util.Date;

/**
 * MQ服务信息VO
 *
 * @author jiayuan.su
 */
public class MqServiceInfoVO extends BaseModel {
    /**
     * 无意义主键
     */
    private Long id;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 服务描述
     */
    private String serviceDesc;
    /**
     * 组ID
     */
    private Long groupId;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 是否删除了: 0-未删除, 1-删除
     *
     * {@link cn.ttpai.mqlistener.admin.common.enums.YesOrNoEnum}
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 逗号: 用于批量保存
     */
    private String comma = ",";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getComma() {
        return comma;
    }

    public void setComma(String comma) {
        this.comma = comma;
    }
}
