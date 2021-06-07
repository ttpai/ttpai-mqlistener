package cn.ttpai.mqlistener.admin.rabbit.constant;

/**
 * Rabbit常量
 *
 * @author jiayuan.su
 */
public final class RabbitConst {

    public static final class Url {

        /**
         * 发送消息
         * 说明: 第1个%s为ip:port, 第2个%s为vhost
         */
        public static final String PUBLISH_TPLT = "http://%s/api/exchanges/%s//publish";
        /**
         * 获取消息
         * 说明: 第1个%s为ip:port, 第2个%s为vhost, 第3个%s为queueName
         */
        public static final String GET_MSG_TPLT = "http://%s/api/queues/%s/%s/get";

        private Url() {}
    }

    private RabbitConst() {}
}
