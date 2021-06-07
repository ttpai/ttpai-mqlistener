package cn.ttpai.mqlistener.core.util;

import cn.ttpai.mqlistener.core.ContainerInfo;
import cn.ttpai.mqlistener.core.ListenerConfig;
import cn.ttpai.mqlistener.core.enums.ListenerStatusEnum;
import cn.ttpai.mqlistener.core.enums.ProcessTypeEnum;
import cn.ttpai.mqlistener.core.exception.MqBaseException;
import cn.ttpai.mqlistener.core.zk.ZooManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 上下文工具类
 *
 * @author jiayuan.su
 */
public final class ContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContextUtil.class);

    /**
     * 容器信息
     */
    private static volatile ContainerInfo containerInfo;

    /**
     * zookeeper管理器
     */
    private static volatile ZooManager zooManager;

    /**
     * 监听器配置 map
     */
    private static volatile Map<String, ListenerConfig> listenerConfigMap = new ConcurrentHashMap<>();

    /**
     * 实际监听器bean map
     */
    private static volatile Map<String, SimpleMessageListenerContainer> realListenerBeanMap = new ConcurrentHashMap<>();

    /**
     * 监听器配置初始化标志 map
     */
    private static volatile Map<String, AtomicBoolean> configInitFlagMap = new ConcurrentHashMap<>();

    public static ContainerInfo getContainerInfo() {
        return containerInfo;
    }

    public static void setContainerInfo(ContainerInfo containerInfo) {
        ContextUtil.containerInfo = containerInfo;
    }

    public static ZooManager getZooManager() {
        return zooManager;
    }

    public static void setZooManager(ZooManager zooManager) {
        ContextUtil.zooManager = zooManager;
    }

    /**
     * 添加监听器配置
     */
    public static void addListenerConfig(String listenerName, ListenerConfig listenerConfig) {
        listenerConfigMap.put(listenerName, listenerConfig);
    }

    /**
     * 获取监听器配置
     */
    public static ListenerConfig getListenerConfig(String listenerName) {
        return listenerConfigMap.get(listenerName);
    }

    /**
     * 添加实际监听器bean
     */
    public static void addRealListenerBean(String listenerName, SimpleMessageListenerContainer realListenerBean) {
        realListenerBeanMap.put(listenerName, realListenerBean);
    }

    /**
     * 添加监听器配置未初始化标志
     */
    public static void addConfigNotInitFlag(String listenerName) {
        configInitFlagMap.put(listenerName, new AtomicBoolean(false));
    }

    /**
     * 更新监听器配置为初始化标志
     */
    public static boolean updateConfigToInitFlag(String listenerName) {
        AtomicBoolean initFlag = configInitFlagMap.get(listenerName);
        if (null == initFlag) {
            // 不应该走这里
            logger.error("warn：监听器[{}]是否初始化标志不存在", listenerName);
            return false;
        }
        if (initFlag.get()) {
            return false;
        }

        return initFlag.compareAndSet(false, true);
    }

    /**
     * 获取实际监听器bean
     */
    public static SimpleMessageListenerContainer getRealListenerBean(String listenerName) {
        return realListenerBeanMap.get(listenerName);
    }

    /**
     * 获取所有监听器的名称
     */
    public static List<String> getAllListenerNameList() {
        return new ArrayList<>(realListenerBeanMap.keySet());
    }

    /**
     * 更新监听器信息
     *
     * @param listenerName 监听器名称
     * @return listenerConfig 监听器配置
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static void updateListenerInfo(String listenerName, ListenerConfig listenerConfig) {
        synchronized (listenerName.intern()) {
            addListenerConfig(listenerName, listenerConfig);
            SimpleMessageListenerContainer realListenerBean = realListenerBeanMap.get(listenerName);
            if (null != realListenerBean) {
                if (null != listenerConfig.getConcurrentConsumers() && listenerConfig.getConcurrentConsumers() > 0) {
                    realListenerBean.setConcurrentConsumers(listenerConfig.getConcurrentConsumers());
                }
                if (null != listenerConfig.getPrefetchCount() && listenerConfig.getPrefetchCount() > 0) {
                    if (realListenerBean.isRunning()) {
                        realListenerBean.stop();
                        realListenerBean.setPrefetchCount(listenerConfig.getPrefetchCount());
                        realListenerBean.start();
                    } else {
                        realListenerBean.setPrefetchCount(listenerConfig.getPrefetchCount());
                    }
                }
                logger.error("info：监听器[{}]的配置为[{}]", listenerName, AssistUtil.toJsonStringByNoError(listenerConfig));
            } else {
                // 不应该走这里
                logger.error("warn：监听器[{}]不存在", listenerName);
            }
        }
    }

    /**
     * 更新监听器信息
     *
     * @param listenerName   监听器名称
     * @param listenerStatus 监听器状态
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static void updateListenerInfo(String listenerName, String listenerStatus) {
        synchronized (listenerName.intern()) {
            SimpleMessageListenerContainer realListenerBean = realListenerBeanMap.get(listenerName);
            if (null != realListenerBean) {
                ListenerConfig listenerConfig = listenerConfigMap.get(listenerName);
                if (ListenerStatusEnum.SUSPENDED.getCode().equals(listenerStatus) && realListenerBean.isRunning()) {
                    realListenerBean.stop();
                    logger.error("info：监听器[{}]处于停止状态", listenerName);
                    return;
                }
                if (ListenerStatusEnum.RUNNING.getCode().equals(listenerStatus) && !realListenerBean.isRunning()) {
                    if (!isConfigValid(listenerConfig)) {
                        logger.error("warn：监听器[{}]配置信息丢失,不进行启动!请检查并前往控制台改配置!",
                                listenerName, new MqBaseException("listener_config_missing", "监听器配置丢失"));
                        return;
                    }

                    realListenerBean.start();
                    logger.error("info：监听器[{}]处于运行状态", listenerName);
                    return;
                }
                logger.error("info：监听器[{}]处于{}状态", listenerName, !realListenerBean.isRunning() ? "停止" : "运行");
            } else {
                // 不应该走这里
                logger.error("warn：监听器[{}]不存在", listenerName);
            }
        }
    }

    /**
     * 释放资源
     *
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static synchronized void shutdown() {
        // 1.关闭所有listener
        Map<String, SimpleMessageListenerContainer> listenerBeanMap = getRealListenerBeanMap();
        if (AssistUtil.isMapEmpty(listenerBeanMap)) {
            logger.error("info：监听器不存在，不进行关闭");
            return;
        }

        for (Map.Entry<String, SimpleMessageListenerContainer> entry : listenerBeanMap.entrySet()) {
            SimpleMessageListenerContainer realListener = entry.getValue();
            realListener.stop();
            realListener.shutdown();
        }

        // 2.释放zookeeper client
        getZooManager().close();
    }

    /**
     * 获取实际监听器map
     */
    private static Map<String, SimpleMessageListenerContainer> getRealListenerBeanMap() {
        return realListenerBeanMap;
    }

    /**
     * 配置是否有效
     */
    private static boolean isConfigValid(ListenerConfig listenerConfig) {
        if (null == listenerConfig) {
            return false;
        }

        // 并发数
        if (!isValidOfConcurrentConsumers(listenerConfig.getConcurrentConsumers())) {
            return false;
        }

        // 1.处理老版本数据
        if (null == listenerConfig.getProcessType()) {
            return isValidOfOldVersion(listenerConfig);
        }
        // 2.处理新版本数据
        return isValidOfNewVersion(listenerConfig);
    }

    /**
     * 老版本数据是否有效
     */
    private static boolean isValidOfOldVersion(ListenerConfig listenerConfig) {
        return StringUtils.isNotBlank(listenerConfig.getExceptionQueue())
                || !Objects.isNull(listenerConfig.getIsReturnHead());
    }

    /**
     * 新版本数据是否有效
     */
    private static boolean isValidOfNewVersion(ListenerConfig listenerConfig) {
        ProcessTypeEnum processTypeEnum = ProcessTypeEnum.getEnum(listenerConfig.getProcessType());

        // 处理类型不存在
        if (null == processTypeEnum) {
            return false;
        }

        // 处理类型存在
        switch (processTypeEnum) {
            case EXCEPTION_QUEUE:
                return StringUtils.isNotBlank(listenerConfig.getExceptionQueue());
            case REPUT_QUEUE:
                return !Objects.isNull(listenerConfig.getIsReturnHead());
            case REPUT_QUEUE_COUNT_DISCARD:
                return !Objects.isNull(listenerConfig.getFailLimit()) && listenerConfig.getFailLimit() > 0;
            case REPUT_QUEUE_COUNT_EXCEPTION_QUEUE:
                return !Objects.isNull(listenerConfig.getFailLimit()) && listenerConfig.getFailLimit() > 0
                        && StringUtils.isNotBlank(listenerConfig.getExceptionQueue());
            default:
                return false;
        }
    }

    /**
     * 并发数是否有效
     */
    private static boolean isValidOfConcurrentConsumers(Integer concurrentConsumers) {
        return !Objects.isNull(concurrentConsumers) && concurrentConsumers > 0;
    }

    private ContextUtil() {
    }
}
