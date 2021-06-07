package cn.ttpai.mqlistener.admin.common.component;

import cn.ttpai.mqlistener.admin.common.component.zk.ZooTreeCacheListener;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * api test
 *
 * @author jiayuan.su
 */
public class ApiTest {

    private CuratorFramework client;

    @Before
    public void before() {
        String connectionString = "localhost:2181";
        RetryPolicy retryPolicy= new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        this.client.start();
    }

    @Test
    public void testZk01() throws Exception {
        TreeCacheListener treeCacheListener = new ZooTreeCacheListener();

        String treeCachePath = "/ttpai_mq_listener";
        TreeCache treeCache = new TreeCache(this.client, treeCachePath);
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();

        TimeUnit.SECONDS.sleep(1000L);
    }

    @Test
    public void testZk02() throws Exception {
        String path = "/test/aa/bb/cc";

        System.out.println(null != this.client.checkExists().forPath(path));
    }

    @Test
    public void testZk03() throws Exception {
        String path = "/test/cc";

        System.out.println(this.client.getData().forPath(path));
    }
}
