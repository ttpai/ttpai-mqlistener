package cn.ttpai.mqlistener.core;

import cn.ttpai.mqlistener.core.util.ContextUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * mq listener容器关闭
 *
 * @author jiayuan.su
 */
public class MqListenerContainerClose implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MqListenerContainerClose.class);

    /**
     * 是否已初始化
     */
    private final AtomicBoolean shutdowned = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (!shutdowned.compareAndSet(false, true)) {
            return;
        }

        logger.error("info：--------------监听器关闭开始--------------");
        ContextUtil.shutdown();
        logger.error("info：--------------监听器关闭结束--------------");
    }
}
