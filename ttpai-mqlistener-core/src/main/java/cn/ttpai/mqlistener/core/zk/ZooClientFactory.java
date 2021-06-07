package cn.ttpai.mqlistener.core.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * CuratorFramework factory bean
 *
 * @author jiayuan.su
 */
public class ZooClientFactory implements FactoryBean<CuratorFramework> {

    /**
     * zookeeper地址
     */
    private String connectionString;

    /**
     * 重试策略
     */
    private RetryPolicy retryPolicy;

    @Override
    public CuratorFramework getObject() {
        CuratorFramework zooClient = CuratorFrameworkFactory.newClient(this.connectionString, this.retryPolicy);
        zooClient.start();

        return zooClient;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}
