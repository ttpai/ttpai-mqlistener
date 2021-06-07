package cn.ttpai.mqlistener.core.constant;

/**
 * 容器常量
 *
 * @author jiayuan.su
 */
public final class ContainerConstant {

    public final class Zoo {

        /**
         * 监听器节点
         */
        public static final String LISTENER_PATH = "/{projectName}/{listenerName}";

        /**
         * 监听器配置节点
         */
        public static final String LISTENER_CONFIG_PATH = "{listenerPath}/config";

        /**
         * 监听器管理员节点
         */
        public static final String LISTENER_MANAGER_PATH = "{listenerPath}/manager";

        /**
         * 监听器描述节点
         */
        public static final String LISTENER_DESC_PATH = "{listenerPath}/desc";

        /**
         * 监听器进程实例节点
         */
        public static final String LISTENER_INSTANCE_PATH = "{listenerPath}/instances/{instance}";

        /**
         * 监听器进程实例节点错误描述
         */
        public static final String INSTANCE_ERROR_PATH = "{instancePath}/errorDesc";

        /**
         * 监听器进程实例是否在线节点
         */
        public static final String INSTANCE_ONLINE_PATH = "{instancePath}/online";

        private Zoo() {
        }
    }

    public final class Mq {

        /**
         * 消息消费的失败次数 key
         */
        public static final String MSG_CONSUMED_FAIL_COUNT_KEY = "Msg-Consumed-Fail-Count";

        private Mq() {
        }
    }

    private ContainerConstant() {
    }
}
