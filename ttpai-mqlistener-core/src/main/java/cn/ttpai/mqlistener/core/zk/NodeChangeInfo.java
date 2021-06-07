package cn.ttpai.mqlistener.core.zk;

/**
 * node cache change信息类
 */
public class NodeChangeInfo<V> {

    /**
     * 节点路径
     */
    private String nodePath;

    /**
     * 调用者自定义对象
     */
    private V obj;

    public NodeChangeInfo() {
    }

    public NodeChangeInfo(String nodePath) {
        this.nodePath = nodePath;
    }

    public NodeChangeInfo(String nodePath, V obj) {
        this.nodePath = nodePath;
        this.obj = obj;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public V getObj() {
        return obj;
    }

    public void setObj(V obj) {
        this.obj = obj;
    }
}
