package cn.ttpai.mqlistener.admin.mq.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

import java.util.Date;

/**
 * MQ服务组VO
 *
 * @author jiayuan.su
 */
public class MqServiceGroupVO extends BaseModel {
    /**
     * 组ID
     */
    private Long id;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 组描述
     */
    private String groupDesc;
    /**
     * 是否删除了: 0-未删除, 1-删除了
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
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
}
