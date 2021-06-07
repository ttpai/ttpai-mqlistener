package cn.ttpai.mqlistener.admin.rabbit.model;

import cn.ttpai.mqlistener.admin.common.model.BaseModel;

/**
 * RabbitMQ基本信息VO
 *
 * @author jiayuan.su
 */
public class RabbitBasisInfoVO extends BaseModel {
    /**
     * ip:端口
     */
    private String ipPort;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public RabbitBasisInfoVO() {
    }

    public RabbitBasisInfoVO(String ipPort, String username, String password) {
        this.ipPort = ipPort;
        this.username = username;
        this.password = password;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
