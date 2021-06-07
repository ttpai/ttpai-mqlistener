package cn.ttpai.mqlistener.admin.zoo.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

/**
 * @Auther: wangtq
 * @Date: 2019/2/19 17:05
 * @Description:
 */
public class NodeVO extends BaseModel {

    /**
     * 节点路径
     */
    private String  path;
    /**
     * 是否启用
     */
    private Integer inUse;
    /**
     * 是否在线
     */
    private Integer online;
    /**
     * 错误数据
     */
    private String errorInfo;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 错误产生时间
     */
    private String errorTime;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getInUse() {
        return inUse;
    }

    public void setInUse(Integer inUse) {
        this.inUse = inUse;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }
}
