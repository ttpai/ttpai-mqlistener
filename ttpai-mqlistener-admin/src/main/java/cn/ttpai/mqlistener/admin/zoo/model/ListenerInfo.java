package cn.ttpai.mqlistener.admin.zoo.model;

import cn.ttpai.mqlistener.admin.zoo.model.ListenerConfig;

/**
 * listener信息
 *
 * @author jiayuan.su
 */
public class ListenerInfo {
    /**
     * 监听器名称
     */
    private String name;
    /**
     * 监听器zookeeper中路径
     */
    private String zooPath;
    /**
     * 监听器的queue
     */
    private String queue;
    /**
     * 监听器的描述信息
     */
    private String desc;
    /**
     * 监听器管理员
     */
    private String manager;
    /**
     * 监听器配置信息
     */
    private ListenerConfig listenerConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZooPath() {
        return zooPath;
    }

    public void setZooPath(String zooPath) {
        this.zooPath = zooPath;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public ListenerConfig getListenerConfig() {
        return listenerConfig;
    }

    public void setListenerConfig(ListenerConfig listenerConfig) {
        this.listenerConfig = listenerConfig;
    }
}
