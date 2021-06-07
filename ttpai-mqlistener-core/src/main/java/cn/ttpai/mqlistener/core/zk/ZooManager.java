package cn.ttpai.mqlistener.core.zk;

import cn.ttpai.mqlistener.core.exception.MqBaseException;
import cn.ttpai.mqlistener.core.util.AssistUtil;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * zookeeper服务
 * 注意：公有方法参数zooPath不包括根节点路径
 *
 * @author jiayuan.su
 */
public class ZooManager {

    private static final Logger logger = LoggerFactory.getLogger(ZooManager.class);

    private static final int DEFAULT_BASE_SLEEP_TIME_MS = 1000;

    private static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * zookeeper客户端
     */
    private CuratorFramework zooClient;

    /**
     * zookeeper根节点
     */
    private String rootPath;

    /**
     * zk根节点在zookeeper服务器是否已存在
     *
     * @return true 节点存在; false 节点不存在
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public boolean isZooRootPathExist() {
        return zooPathExist(this.rootPath);
    }

    /**
     * zk节点在zookeeper服务器是否已存在
     *
     * @param zooPath zk节点
     * @return true 节点存在; false 节点不存在
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public boolean isZooPathExist(String zooPath) {
        return zooPathExist(getFullPath(zooPath));
    }

    /**
     * 从zookeeper中获取某节点下的数据
     *
     * @param zooPath zk节点
     * @return String 节点对应数据
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public String getDataFromZoo(String zooPath) {
        final String fullPath = getFullPath(zooPath);

        if (!zooPathExist(fullPath)) {
            return null;
        }

        try {
            byte[] byteData = this.zooClient.getData().forPath(fullPath);

            return null != byteData && byteData.length > 0 ? new String(byteData, StandardCharsets.UTF_8) : null;
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 从zookeeper中获取根节点下的所有子节点
     *
     * @return List<String> 子节点列表
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public List<String> getRootChildrenFromZoo() {
        return childrenFromZoo(this.rootPath);
    }

    /**
     * 从zookeeper中获取某节点下的所有子节点
     *
     * @param zooPath zk节点
     * @return List<String> 子节点列表
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public List<String> getChildrenFromZoo(String zooPath) {
        return childrenFromZoo(getFullPath(zooPath));
    }

    /**
     * 往zookeeper某节点下写入数据
     *
     * @param zooPath zk节点
     * @param data    节点数据
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public void setData2Zoo(String zooPath, String data) {
        try {
            zooClient.setData().forPath(getFullPath(zooPath), data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 删除zookeeper中某节点
     *
     * @param zooPath zk节点
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public void deleteZooPath(String zooPath) {
        try {
            zooClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(getFullPath(zooPath));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 创建zk节点
     *
     * @param zooPath zk节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void createZooPath(String zooPath) {
        try {
            zooClient.create().creatingParentsIfNeeded().forPath(getFullPath(zooPath));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 创建zk节点，并设置节点数据
     *
     * @param zooPath zk节点
     * @param data    节点数据
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void createZooPath(String zooPath, String data) {
        if (null == data) {
            createZooPath(zooPath);
            return;
        }

        try {
            zooClient.create().creatingParentsIfNeeded().forPath(getFullPath(zooPath),
                    data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 创建zk临时节点
     *
     * @param zooPath zk节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void createZooPathOfTemp(String zooPath) {
        try {
            zooClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(getFullPath(zooPath));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    public boolean isZooPathExistByCreateParent(String zooPath) {
        try {
            return null != zooClient.checkExists().creatingParentContainersIfNeeded().forPath(getFullPath(zooPath));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 添加节点数据变化监听
     *
     * @param nodeChangeInfo     传递的信息
     * @param nodeChangeExecutor 回调执行器，由调用者实现
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void addNodeCacheListener(final NodeChangeInfo<?> nodeChangeInfo,
                                     final NodeChangeExecutor nodeChangeExecutor) throws Exception {
        addNodeCacheListener(true, nodeChangeInfo, nodeChangeExecutor);
    }

    /**
     * 添加节点数据变化监听
     *
     * @param isInitSync         初始化时是否同步节点信息
     * @param nodeChangeInfo     传递的信息
     * @param nodeChangeExecutor 回调执行器，由调用者实现
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void addNodeCacheListener(boolean isInitSync, final NodeChangeInfo<?> nodeChangeInfo,
                                     final NodeChangeExecutor nodeChangeExecutor) throws Exception {
        if (AssistUtil.isBlank(nodeChangeInfo.getNodePath())) {
            throw new IllegalArgumentException("node path cannot be blank");
        }

        final NodeCache nodeCache = new NodeCache(this.zooClient, getFullPath(nodeChangeInfo.getNodePath()));

        nodeCache.getListenable().addListener(new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                ChildData childData = nodeCache.getCurrentData();

                // 节点数据变化时才通知(为null时代表节点删除)
                if (null != childData) {
                    nodeChangeExecutor.execute(nodeChangeInfo,
                            null != childData.getData() && childData.getData().length > 0
                                    ? new String(childData.getData(), StandardCharsets.UTF_8)
                                    : null);
                }
            }
        });

        nodeCache.start(!isInitSync);
    }

    /**
     * 添加断线重连时的监听器
     *
     * @param connectionLostExecutor 连接丢失时处理器
     * @author jiayuan.su
     * @date 2019年07月01日
     */
    public void addReConnectListener(final ConnectionLostExecutor connectionLostExecutor) {
        final CuratorFramework zkClient = this.zooClient;
        zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.LOST) {
                    while (true) {
                        try {
                            if (zkClient.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                                connectionLostExecutor.execute();
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("zookeeper断线重连处理时异常", e);
                        }
                    }
                }
            }
        });
    }

    /**
     * 释放资源
     *
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public void close() {
        this.zooClient.close();
    }

    /**
     * 注入属性
     */
    public void setZooClient(CuratorFramework zooClient) {
        this.zooClient = zooClient;
    }

    /**
     * 注入属性
     */
    public void setDefaultZooClient(String connectionString) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME_MS, DEFAULT_MAX_RETRIES);
        this.zooClient = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        this.zooClient.start();
    }

    /**
     * 注入属性
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * zk节点是否存在
     */
    private boolean zooPathExist(String zooPath) {
        try {
            return null != this.zooClient.checkExists().forPath(zooPath);
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 获取子节点
     */
    private List<String> childrenFromZoo(String zooPath) {
        try {
            return zooClient.getChildren().forPath(zooPath);
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new MqBaseException("system_error", "zookeeper error", e);
        }
    }

    /**
     * 拿到完整zookeeper节点
     */
    private String getFullPath(String zooPath) {
        return this.rootPath + cleanStr(zooPath);
    }

    /**
     * 洗一下字符串
     */
    private String cleanStr(String str) {
        if (AssistUtil.isBlank(str)) {
            throw new IllegalArgumentException("value can't blank");
        }
        return str;
    }
}
