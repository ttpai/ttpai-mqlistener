package cn.ttpai.mqlistener.core.zk;

/**
 * node cache change执行器
 */
public interface NodeChangeExecutor {

    /**
     * 执行方法
     *
     * @param nodeChangeInfo {@link NodeChangeInfo}
     * @param data           节点数据，可能返回null
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    void execute(NodeChangeInfo<?> nodeChangeInfo, String data);
}
