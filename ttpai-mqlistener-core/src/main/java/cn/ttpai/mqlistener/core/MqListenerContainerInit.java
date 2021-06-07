package cn.ttpai.mqlistener.core;

import cn.ttpai.mqlistener.core.enums.ListenerStatusEnum;
import cn.ttpai.mqlistener.core.exception.MqBaseException;
import cn.ttpai.mqlistener.core.util.AppUtil;
import cn.ttpai.mqlistener.core.util.AssistUtil;
import cn.ttpai.mqlistener.core.util.ContextUtil;
import cn.ttpai.mqlistener.core.zk.ConnectionLostExecutor;
import cn.ttpai.mqlistener.core.zk.NodeChangeExecutor;
import cn.ttpai.mqlistener.core.zk.NodeChangeInfo;
import cn.ttpai.mqlistener.core.zk.ZooManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * mq listener容器初始化
 *
 * @author jiayuan.su
 */
public class MqListenerContainerInit implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MqListenerContainerInit.class);

    /**
     * 操作zookeeper的类
     */
    @Autowired
    private ZooManager zooManager;

    /**
     * 属性中心
     */
    @Autowired
    private BaseConfig baseConfig;

    /**
     * 是否已初始化
     */
    private final AtomicBoolean inited = new AtomicBoolean(false);

    /**
     * spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!inited.compareAndSet(false, true)) {
            return;
        }

        this.applicationContext = event.getApplicationContext();

        logger.error("info：--------------监听器初始化开始--------------");
        doInit();
        logger.error("info：--------------监听器初始化结束--------------");
    }

    /**
     * 初始化
     */
    private void doInit() {
        // 构建上下文信息
        buildContextInfo();
        // 处理mq listener
        handleMqListener();
        // 添加节点监听器
        addNodeListener();
        // 添加断线重连监听器
        addReConnectListener();
    }

    /**
     * 构建上下文信息
     */
    private void buildContextInfo() {
        // 1.获取基本信息
        String projectName = baseConfig.getProjectName();
        if (AssistUtil.isBlank(projectName)) {
            logger.error("项目名不能为空");
            throw new MqBaseException("info_missing", "项目名不能为空");
        }

        String ip = null;
        try {
            ip = AppUtil.getLocalIp();

            if (AssistUtil.isBlank(ip)) {
                logger.error("ip地址不能为空");
                throw new MqBaseException("info_missing", "ip地址不能为空");
            }
        } catch (Exception e) {
            logger.error("ip地址获取失败", e);
            throw new MqBaseException("info_missing", "ip地址获取失败", e);
        }

        String port = null;
        try {
            port = AppUtil.getLocalPort();

            if (AssistUtil.isBlank(port)) {
                throw new MqBaseException("info_missing", "端口不能为空");
            }
        } catch (Exception e) {
            logger.error("端口获取失败", e);
            throw new MqBaseException("info_missing", "端口获取失败", e);
        }

        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setIp(ip);
        containerInfo.setPort(port);
        containerInfo.setIpPort(AssistUtil.join(AssistUtil.SYMBOL_AT, ip, port));
        containerInfo.setProjectName(projectName);

        // 2.放入上下文
        ContextUtil.setContainerInfo(containerInfo);
    }

    /**
     * 处理mq listener
     */
    private void handleMqListener() {
        Map<String, MqListener> mqListenerMap = this.applicationContext.getBeansOfType(MqListener.class);
        if (AssistUtil.isMapEmpty(mqListenerMap)) {
            logger.error("--------------项目中不存在mq listener, 不进行mq listener的处理--------------");
            return;
        }

        final ContainerInfo containerInfo = ContextUtil.getContainerInfo();
        for (Map.Entry<String, MqListener> entry : mqListenerMap.entrySet()) {
            // 1.用户配置的listener bean
            final String listenerName = entry.getKey();
            final MqListener listenerBean = entry.getValue();

            // 2.处理实际listener bean
            SimpleMessageListenerContainer realListenerBean = new SimpleMessageListenerContainer();

            realListenerBean.setConnectionFactory(listenerBean.getConnectionFactory());
            realListenerBean.setQueueNames(listenerBean.getQueue());
            realListenerBean.setAcknowledgeMode(AcknowledgeMode.MANUAL);
            realListenerBean.setMessageListener(listenerBean);

            ContextUtil.addRealListenerBean(listenerName, realListenerBean);
            ContextUtil.addConfigNotInitFlag(listenerName);

            // 3.往zk中同步数据
            syncData2Zk(containerInfo.getProjectName(), listenerName, listenerBean.getQueue(),
                    containerInfo.getIpPort());
        }

    }

    /**
     * 添加节点监听器
     */
    private void addNodeListener() {
        List<String> listenerNameList = ContextUtil.getAllListenerNameList();
        if (CollectionUtils.isEmpty(listenerNameList)) {
            logger.error("--------------项目中不存在mq listener, 不进行mq listener的zk监听--------------");
            return;
        }

        final ContainerInfo containerInfo = ContextUtil.getContainerInfo();

        CountDownLatch countDownLatch = new CountDownLatch(listenerNameList.size());
        // 从zk中获取listener配置
        for (String listenerName : listenerNameList) {
            String listenerPath = AssistUtil.getListenerPath(containerInfo.getProjectName(), listenerName);

            // listener配置路径zk监听
            String listenerConfigPath = AssistUtil.getListenerConfigPath(listenerPath);
            NodeChangeInfo<String> nodeChangeInfo4Config = new NodeChangeInfo(listenerConfigPath, listenerName);
            try {
                this.zooManager.addNodeCacheListener(nodeChangeInfo4Config, new NodeChangeExecutor() {

                    @Override
                    public void execute(NodeChangeInfo<?> nodeChangeInfo, String data) {
                        String listenerName = (String) nodeChangeInfo.getObj();

                        ListenerConfig listenerConfig = null;
                        if (AssistUtil.isBlank(data)) {
                            listenerConfig = new ListenerConfig();
                        } else {
                            listenerConfig = parseListenerConfig(data);
                        }

                        ContextUtil.updateListenerInfo(listenerName, listenerConfig);

                        // 是否初始化配置，true是 false否
                        if (updateConfigInit(listenerName)) {
                            logger.error("监听器[{}]的配置已被初始化", listenerName);
                            countDownLatch.countDown();
                        }
                    }
                });
            } catch (Exception e) {
                logger.error("配置路径ZK监听异常", e);
                throw new MqBaseException("zk_lisenter_error", "ZK监听异常", e);
            }
        }

        // 闭锁，后面依赖前面的结果
        try {
            countDownLatch.await();
        } catch (Exception e) {
            logger.error("配置路径ZK监听异常", e);
            throw new MqBaseException("zk_lisenter_error", "ZK监听异常", e);
        }

        // 从zk中获取是/否启动标志，根据是启动标志及上一步配置进行listener的启动
        for (String listenerName : listenerNameList) {
            String listenerPath = AssistUtil.getListenerPath(containerInfo.getProjectName(), listenerName);

            // listener实例路径 zk监听
            String instancePath = AssistUtil.getListenerInstancePath(listenerPath, containerInfo.getIpPort());
            NodeChangeInfo<String> nodeChangeInfo4Instance = new NodeChangeInfo<>(instancePath, listenerName);
            try {
                this.zooManager.addNodeCacheListener(nodeChangeInfo4Instance, new NodeChangeExecutor() {

                    @Override
                    public void execute(NodeChangeInfo<?> nodeChangeInfo, String data) {
                        if (AssistUtil.isBlank(data)) {
                            return;
                        }

                        String listenerName = (String) nodeChangeInfo.getObj();
                        String listenerStatus = parseListenerStatus(data);

                        ContextUtil.updateListenerInfo(listenerName, listenerStatus);
                    }
                });
            } catch (Exception e) {
                logger.error("实例路径ZK监听异常", e);
                throw new MqBaseException("zk_lisenter_error", "ZK监听异常", e);
            }
        }
    }

    /**
     * 往zk中同步数据
     */
    private void syncData2Zk(String projectName, String listenerName, String queue, String instance) {
        logger.error("-------------------------------------------------------------------");
        // 监听器节点
        String listenerPath = AssistUtil.getListenerPath(projectName, listenerName);
        if (!this.zooManager.isZooPathExist(listenerPath)) {
            logger.error("info：创建[{}]对应监听器节点[{}]", listenerName, listenerPath);
            this.zooManager.createZooPath(listenerPath, queue);
        } else {
            // 队列可能更改，需重新设置队列
            this.zooManager.setData2Zoo(listenerPath, queue);
        }
        // 监听器配置节点
        String listenerConfigPath = AssistUtil.getListenerConfigPath(listenerPath);
        if (!this.zooManager.isZooPathExist(listenerConfigPath)) {
            logger.error("info：创建[{}]对应监听器配置节点[{}]", listenerName, listenerConfigPath);
            this.zooManager.createZooPath(listenerConfigPath, "");
        }
        // 监听器管理员节点
        String listenerManagerPath = AssistUtil.getListenerManagerPath(listenerPath);
        if (!this.zooManager.isZooPathExist(listenerManagerPath)) {
            logger.error("info：创建[{}]对应监听器管理员节点[{}]", listenerName, listenerManagerPath);
            this.zooManager.createZooPath(listenerManagerPath, "");
        }
        // 监听器描述节点
        String listenerDescPath = AssistUtil.getListenerDescPath(listenerPath);
        if (!this.zooManager.isZooPathExist(listenerDescPath)) {
            logger.error("info：创建[{}]对应监听器描述节点[{}]", listenerName, listenerDescPath);
            this.zooManager.createZooPath(listenerDescPath, "");
        }

        // 监听器实例节点
        String instancePath = AssistUtil.getListenerInstancePath(listenerPath, instance);
        if (!this.zooManager.isZooPathExist(instancePath)) {
            logger.error("info：创建[{}]对应监听器实例节点[{}]", listenerName, instancePath);
            this.zooManager.createZooPath(instancePath, ListenerStatusEnum.SUSPENDED.getCode());
        }
        // 监听器实例错误描述节点
        String instanceErrorPath = AssistUtil.getInstanceErrorPath(instancePath);
        if (!this.zooManager.isZooPathExist(instanceErrorPath)) {
            logger.error("info：创建[{}]对应监听器实例错误节点[{}]", listenerName, instanceErrorPath);
            this.zooManager.createZooPath(instanceErrorPath, "");
        } else {
            // 启动时清除上次错误
            this.zooManager.setData2Zoo(instanceErrorPath, "");
        }
        // 监听器实例是否在线节点
        String instanceOnlinePath = AssistUtil.getInstanceOnlinePath(instancePath);
        if (this.zooManager.isZooPathExist(instanceOnlinePath)) {
            // 关闭后马上启动时，可能临时节点还未失效
            logger.error("info：删除[{}]对应监听器实例在线节点[{}]", listenerName, instanceOnlinePath);
            try {
                this.zooManager.deleteZooPath(instanceOnlinePath);
            } catch (Exception e) {
                logger.error("info：删除[{}]对应监听器实例在线节点[{}]成功(异常请忽略)", instanceOnlinePath, listenerName, e);
            }
        }
        logger.error("info：创建[{}]对应监听器实例在线节点[{}]", listenerName, instanceOnlinePath);
        this.zooManager.createZooPathOfTemp(instanceOnlinePath);
    }

    /**
     * 解析json成ListenerConfig
     */
    private ListenerConfig parseListenerConfig(String data) {
        try {
            return AssistUtil.parseObject(data, ListenerConfig.class);
        } catch (Exception e) {
            logger.error("zk listener config节点数据[{}]解析异常", data, e);
            throw new MqBaseException("data_error", "节点数据解析异常", e);
        }
    }

    /**
     * 解析listener的状态
     */
    private String parseListenerStatus(String data) {
        if (!ListenerStatusEnum.isExist(data)) {
            logger.error("zk listener节点数据[{}]解析异常", data);
            throw new MqBaseException("data_illegal", "zk listener节点数据非法");
        }
        return data;
    }

    /**
     * 添加断线重连监听器
     */
    private void addReConnectListener() {
        this.zooManager.addReConnectListener(new ConnectionLostExecutor() {

            @Override
            public void execute() {
                List<String> listenerNameList = ContextUtil.getAllListenerNameList();
                if (CollectionUtils.isEmpty(listenerNameList)) {
                    logger.error("--------------项目中不存在mq listener, 不进行mq listener的断线重连处理--------------");
                    return;
                }

                final ContainerInfo containerInfo = ContextUtil.getContainerInfo();
                for (String listenerName : listenerNameList) {
                    // 监听器节点
                    String listenerPath = AssistUtil.getListenerPath(containerInfo.getProjectName(), listenerName);
                    // 监听器实例节点
                    String instancePath = AssistUtil.getListenerInstancePath(listenerPath, containerInfo.getIpPort());
                    // 监听器实例是否在线节点
                    String instanceOnlinePath = AssistUtil.getInstanceOnlinePath(instancePath);
                    // 不存在时进行创建
                    if (!MqListenerContainerInit.this.zooManager.isZooPathExist(instanceOnlinePath)) {
                        logger.error("info：断线重连时创建[{}]对应监听器实例在线节点[{}]", listenerName, instanceOnlinePath);
                        MqListenerContainerInit.this.zooManager.createZooPathOfTemp(instanceOnlinePath);
                    }
                }
            }
        });
    }

    /**
     * 更新监听器配置为初始化状态
     */
    private boolean updateConfigInit(String listenerName) {
        return ContextUtil.updateConfigToInitFlag(listenerName);
    }
}
