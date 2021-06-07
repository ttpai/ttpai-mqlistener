package cn.ttpai.mqlistener.core.zk;

/**
 * 连接状态丢失时执行器
 *
 * @author jiayuan.su
 */
public interface ConnectionLostExecutor {

    void execute();
}
