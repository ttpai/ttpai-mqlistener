package cn.ttpai.mqlistener.admin.mq.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

import java.util.Date;

/**
 * MQ修复VO
 *
 * @author jiayuan.su
 */
public class MqFixVO extends BaseModel {
    /**
     * 无意义主键ID
     */
    private Long id;
    /**
     * 修复名称
     */
    private String fixName;
    /**
     * 负责人
     */
    private String chargePerson;
    /**
     * 修复状态: 0-修复中, 1-已结束
     *
     * {@link cn.ttpai.mqlistener.admin.mq.enums.MQFixStatusEnum}
     */
    private Integer fixStatus;
    /**
     * 修复备注
     */
    private String fixRemark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    /** ----- 以上PO ---------------------------------------------------------------- */

    /**
     * 负责人like查询
     */
    private String chargePersonLike;
    /**
     * 需要修复的数量
     */
    private Integer needFixNum;
    /**
     * 待修复的数量
     */
    private Integer waitFixNum;
    /**
     * 修复中的数量
     */
    private Integer inFixNum;
    /**
     * 修复功能的数量
     */
    private Integer fixSuccessNum;
    /**
     * 修复异常的数量
     */
    private Integer fixErrorNum;
    /**
     * 是否有需要修复的消息
     * 说明: 1-是, 0-否
     */
    private Integer hasNeedFix;
    /**
     * 是否所有修复成功
     * 说明: 1-是, 0-否
     */
    private Integer allFixSuccess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFixName() {
        return fixName;
    }

    public void setFixName(String fixName) {
        this.fixName = fixName;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public Integer getFixStatus() {
        return fixStatus;
    }

    public void setFixStatus(Integer fixStatus) {
        this.fixStatus = fixStatus;
    }

    public String getFixRemark() {
        return fixRemark;
    }

    public void setFixRemark(String fixRemark) {
        this.fixRemark = fixRemark;
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

    public String getChargePersonLike() {
        return chargePersonLike;
    }

    public void setChargePersonLike(String chargePersonLike) {
        this.chargePersonLike = chargePersonLike;
    }

    public Integer getNeedFixNum() {
        return needFixNum;
    }

    public void setNeedFixNum(Integer needFixNum) {
        this.needFixNum = needFixNum;
    }

    public Integer getWaitFixNum() {
        return waitFixNum;
    }

    public void setWaitFixNum(Integer waitFixNum) {
        this.waitFixNum = waitFixNum;
    }

    public Integer getInFixNum() {
        return inFixNum;
    }

    public void setInFixNum(Integer inFixNum) {
        this.inFixNum = inFixNum;
    }

    public Integer getFixSuccessNum() {
        return fixSuccessNum;
    }

    public void setFixSuccessNum(Integer fixSuccessNum) {
        this.fixSuccessNum = fixSuccessNum;
    }

    public Integer getFixErrorNum() {
        return fixErrorNum;
    }

    public void setFixErrorNum(Integer fixErrorNum) {
        this.fixErrorNum = fixErrorNum;
    }

    public Integer getHasNeedFix() {
        return hasNeedFix;
    }

    public void setHasNeedFix(Integer hasNeedFix) {
        this.hasNeedFix = hasNeedFix;
    }

    public Integer getAllFixSuccess() {
        return allFixSuccess;
    }

    public void setAllFixSuccess(Integer allFixSuccess) {
        this.allFixSuccess = allFixSuccess;
    }
}
