package cn.ttpai.mqlistener.admin.zoo.constant;

public final class Constants {

	/**
	 * 正斜杠
	 */
	public static final String FORWARD_SLASH = "/";
	/**
	 * 字符串--数字0
	 */
	public static final String STR_ZERO = "0";
	/**
	 * 字符串--数字1
	 */
	public static final String STR_ONE = "1";

	public static class ZooNode {

		/**
		 * 节点路径--某一应用
		 * 即：/{projectName}
		 */
		public static final String NODE_PATH_PROJECT = "/{projectName}";
		/**
		 * 节点路径--某一应用下的listerner
		 * 即：/{projectName}/{listenerName}
		 */
		public static final String NODE_PATH_PROJECT_LISTENER = NODE_PATH_PROJECT + "/{listenerName}";
		/**
		 * 节点路径--mq listener的配置
		 * 即：{/项目名/监听器}/config
		 */
		public static final String NODE_PATH_LISTENER_CONFIG = "{listenerPath}/config";
		/**
		 * 节点路径--mq listener的描述
		 * 即：{/项目名/监听器}/desc
		 */
		public static final String NODE_PATH_LISTENER_DESC = "{listenerPath}/desc";
		/**
		 * 节点路径--mq listener的描述
		 * 即：/项目名/监听器/manager
		 */
		public static final String NODE_PATH_LISTENER_MANAGER = "{listenerPath}/manager";
		/**
		 * 节点路径--mq listener的所有实例
		 * 即：{/项目名/监听器}/instances
		 */
		public static final String NODE_PATH_LISTENER_INSTANCES = "{listenerPath}/instances";
		/**
		 * 节点路径--mq listener的具体实例
		 * 即：{/项目名/监听器/instances}/{instance}
		 */
		public static final String NODE_PATH_LISTENER_INSTANCE = "{instancesPath}/{instance}";
		/**
		 * 节点路径--mq listener的实例的是否在线
		 * 即：{/projectName/listenerName/instances/instance}/online
		 */
		public static final String NODE_PATH_INSTANCE_ONLINE = "{instancePath}/online";
		/**
		 * 节点路径--mq listener的实例的错误描述
		 * 即：{/projectName/listenerName/instances/instance}/errorDesc
		 */
		public static final String NODE_PATH_INSTANCE_ERROR_DESC = "{instancePath}/errorDesc";

	    private ZooNode() {}
    }

	private Constants() {}
}
