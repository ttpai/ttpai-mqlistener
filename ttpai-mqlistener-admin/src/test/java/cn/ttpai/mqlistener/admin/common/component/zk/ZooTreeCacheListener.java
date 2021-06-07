package cn.ttpai.mqlistener.admin.common.component.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @author jiayuan.su
 */
public class ZooTreeCacheListener implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        System.out.println(event.getType());
    }
}
