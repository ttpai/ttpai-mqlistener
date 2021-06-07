package cn.ttpai.mqlistener.core;

/**
 * 容器的一些信息
 *
 * @author jiayuan.su
 */
public class ContainerInfo {

    /**
     * 容器ip地址
     */
    private String ip;

    /**
     * 容器端口
     */
    private String port;

    /**
     * ip@port
     */
    private String ipPort;

    /**
     * 项目名
     */
    private String projectName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
