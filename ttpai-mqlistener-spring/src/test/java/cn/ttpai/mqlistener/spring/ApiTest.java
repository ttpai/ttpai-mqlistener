package cn.ttpai.mqlistener.spring;

import cn.ttpai.mqlistener.core.zk.NodeChangeExecutor;
import cn.ttpai.mqlistener.core.zk.NodeChangeInfo;
import cn.ttpai.mqlistener.core.zk.ZooManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * api test
 *
 * @author jiayuan.su
 */
@Ignore
public class ApiTest {

    private CuratorFramework client;

    @Before
    public void before() {
        String connectionString = "192.168.8.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        this.client.start();
    }

    @Test
    public void testExceptionShow() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("参数异常");
        RuntimeException runtimeException = new RuntimeException("运行时异常", illegalArgumentException);

        System.out.println(ExceptionUtils.getStackTrace(runtimeException));
    }

    @Test
    public void testNodeCache() throws Exception {
        ZooManager zooManager = new ZooManager();
        zooManager.setZooClient(client);
        zooManager.setRootPath("/ttpai_mq_listener");

        for (int i = 0; i < 10; i++) {
            testNodeCacheAssist(zooManager, i + 1);
        }
    }

    private void testNodeCacheAssist(ZooManager zooManager, int seqNum) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        AtomicInteger count = new AtomicInteger(1);

        NodeChangeInfo<String> nodeChangeInfoConfig = new NodeChangeInfo<>();
        nodeChangeInfoConfig.setNodePath("/aa/bbb/config");
        nodeChangeInfoConfig.setObj("测试");
        zooManager.addNodeCacheListener(true, nodeChangeInfoConfig, new NodeChangeExecutor() {

            @Override
            public void execute(NodeChangeInfo<?> nodeChangeInfo, String data) {
                System.out.println(
                        String.format("==%s:%s=========%s=============", seqNum, count.getAndIncrement(), data));
                countDownLatch.countDown();
            }
        });

        NodeChangeInfo<String> nodeChangeInfoDesc = new NodeChangeInfo<>();
        nodeChangeInfoDesc.setNodePath("/aa/bbb/desc");
        nodeChangeInfoDesc.setObj("测试");
        zooManager.addNodeCacheListener(true, nodeChangeInfoDesc, new NodeChangeExecutor() {

            @Override
            public void execute(NodeChangeInfo<?> nodeChangeInfo, String data) {
                System.out.println(
                        String.format("--%s:%s----------%s------------", seqNum, count.getAndIncrement(), data));
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }
}
