package cn.ttpai.mqlistener.admin.common.model;

import com.alibaba.fastjson.JSON;

/**
 * model的基础类
 *
 * @author jiayuan.su
 */
public class BaseModel {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
