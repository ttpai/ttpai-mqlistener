package cn.ttpai.mqlistener.admin.common.util;

/**
 * 属性文件公共类
 *
 * @Description:TODO
 * @author: 金砖
 * @date: 2014年12月19日 下午4:22:52
 *         久兴信息技术(上海)有限公司
 */
public class PropertiesUtils extends cn.ttpai.mqlistener.admin.common.component.property.PropertiesUtils {

    public static String getContextProperty(String name) {
        return cn.ttpai.mqlistener.admin.common.component.property.PropertiesUtils.getString(name, "");
    }

    public static String getContextProperty(String name, String defaultValue) {
        return cn.ttpai.mqlistener.admin.common.component.property.PropertiesUtils.getString(name, defaultValue);
    }

    public static Integer getInt(String name) {
        return cn.ttpai.mqlistener.admin.common.component.property.PropertiesUtils.getInteger(name, null);
    }

    public static Integer getInt(String name, Integer defaultValue) {
        return cn.ttpai.mqlistener.admin.common.component.property.PropertiesUtils.getInteger(name, defaultValue);
    }
}
