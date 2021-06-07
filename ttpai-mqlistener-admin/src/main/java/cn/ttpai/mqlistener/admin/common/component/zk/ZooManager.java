package cn.ttpai.mqlistener.admin.common.component.zk;

import cn.ttpai.mqlistener.admin.common.exception.SystemException;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;

import org.apache.curator.framework.CuratorFramework;
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
            throw new SystemException(e);
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
     * @param data 节点数据
     * @author jiayuan.su
     * @date 2019年04月16日
     */
    public void setData2Zoo(String zooPath, String data) {
        try {
            zooClient.setData().forPath(getFullPath(zooPath), data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new SystemException(e);
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
            throw new SystemException(e);
        }
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
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * zk节点是否存在
     */
    private boolean zooPathExist(String zooPath) {
        try {
            if (null != this.zooClient.checkExists().forPath(zooPath)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("zookeeper error", e);
            throw new SystemException(e);
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
            throw new SystemException(e);
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
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException("value can't blank");
        }
        return str;
    }
}
