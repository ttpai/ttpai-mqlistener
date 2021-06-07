package cn.ttpai.mqlistener.spring.demo.consumer.listener;

import cn.ttpai.mqlistener.core.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * testListener
 *
 * @author jiayuan.su
 */
public class TestListener extends AbstractListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void listener(String msg) throws Exception {
        logger.info("收到的消息体为: {}", msg);
    }
}
