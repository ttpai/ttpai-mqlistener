package cn.ttpai.mqlistener.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cn.ttpai.mqlistener.core.constant.ContainerConstant;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 辅助工具类
 *
 * @author jiayuan.su
 */
public final class AssistUtil {

    private static final Logger logger = LoggerFactory.getLogger(AssistUtil.class);

    public static final String SYMBOL_AT = "@";

    public static final String SYMBOL_ERROR_SPLIT = "$-$";

    /**
     * 检查字符串是否blank
     *
     * @param str 待检查字符串
     * @return true blank; false not blank
     * @author jiayuan.su
     * @date 2019年04月09日
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 检查字符串是否not blank
     *
     * @param str 待检查字符串
     * @return true not blank; false blank
     * @author jiayuan.su
     * @date 2019年04月09日
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 连接字符串
     *
     * @param flag 分隔符
     * @param str  字符串
     * @return
     * @author jiayuan.su
     * @date 2019年01月08日
     */
    public static String join(String flag, Object... str) {
        return StringUtils.join(str, flag);
    }

    /**
     * 检查map是不是empty
     *
     * @param map
     * @return true empty; false not empty
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static boolean isMapEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 检查map是不是not empty
     *
     * @param map
     * @return true not empty; false empty
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static boolean isMapNotEmpty(Map<?, ?> map) {
        return !isMapEmpty(map);
    }

    /**
     * 解析json成对象
     *
     * @param text  待解析字符串
     * @param clazz 解析目录类
     * @return features 特性
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static final <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text, clazz, new Feature[0]);
    }

    /**
     * 将对象转成json
     *
     * @param obj 待转对象
     * @return String json字符串
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String toJsonString(Object obj) {
        return null != obj ? JSON.toJSONString(obj, new SerializerFeature[0]) : "";
    }

    /**
     * 将对象转成json
     * 异常时返回空字符串
     *
     * @param obj 待转对象
     * @return String json字符串
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String toJsonStringByNoError(Object obj) {
        try {
            return null != obj ? toJsonString(obj) : "";
        } catch (Exception e) {
            logger.error("error：转json失败", e);
            return "";
        }
    }

    /**
     * 获取当前时间的字符串
     * yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的字符串
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getCurDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取监听器节点
     *
     * @param projectName
     * @param listenerName
     * @return String
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getListenerPath(String projectName, String listenerName) {
        return StrFillUtil.item("projectName", projectName).item("listenerName", listenerName)
                .target(ContainerConstant.Zoo.LISTENER_PATH);
    }

    /**
     * 获取监听器配置节点
     *
     * @param listenerPath 监听器节点
     * @return String 监听器配置节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getListenerConfigPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(ContainerConstant.Zoo.LISTENER_CONFIG_PATH);
    }

    /**
     * 获取监听器管理员节点
     *
     * @param listenerPath 监听器节点
     * @return String 监听器管理员节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getListenerManagerPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(ContainerConstant.Zoo.LISTENER_MANAGER_PATH);
    }

    /**
     * 获取监听器描述节点
     *
     * @param listenerPath 监听器节点
     * @return String 监听器描述节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getListenerDescPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(ContainerConstant.Zoo.LISTENER_DESC_PATH);
    }

    /**
     * 获取监听器实例节点
     *
     * @param listenerPath 监听器节点
     * @param instance     监听器实例
     * @return String 监听器实例节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getListenerInstancePath(String listenerPath, String instance) {
        return StrFillUtil.item("listenerPath", listenerPath).item("instance", instance)
                .target(ContainerConstant.Zoo.LISTENER_INSTANCE_PATH);
    }

    /**
     * 获取监听器进程实例节点错误描述
     *
     * @param instancePath 监听器进程实例
     * @return String 监听器进程实例节点错误描述
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getInstanceErrorPath(String instancePath) {
        return StrFillUtil.item("instancePath", instancePath).target(ContainerConstant.Zoo.INSTANCE_ERROR_PATH);
    }

    /**
     * 获取监听器进程实例是否在线节点
     *
     * @param instancePath 监听器进程实例
     * @return String 监听器进程实例是否在线节点
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    public static String getInstanceOnlinePath(String instancePath) {
        return StrFillUtil.item("instancePath", instancePath).target(ContainerConstant.Zoo.INSTANCE_ONLINE_PATH);
    }

    /**
     * 获取监听器实例节点错误描述值
     *
     * @param msgStr   消息数据
     * @param curDate  异常时间
     * @param errorStr 错误描述
     * @return String 错误描述值
     * @author jiayuan.su
     * @date 2019年04月19日
     */
    public static String getErrorDesc(String msgStr, String curDate, String errorStr) {
        // 此处msgStr可能为json字符串，即{}符号与StrFillUtil冲突
        // 用字符串拼接方式处理
        StringBuilder resultBuild = new StringBuilder();
        resultBuild.append(msgStr);
        resultBuild.append(SYMBOL_ERROR_SPLIT);
        resultBuild.append(curDate);
        resultBuild.append(SYMBOL_ERROR_SPLIT);
        resultBuild.append(errorStr);

        return resultBuild.toString();
    }

    /**
     * 获取收件人数组
     *
     * @param receivers 收件人
     * @return 收件人数组
     * @author jiayuan.su
     * @date 2019年07月01日
     */
    public static String[] getReceiverArray(String receivers) {
        if (isBlank(receivers)) {
            return new String[0];
        }

        return receivers.split(",");
    }

    /**
     * 解析json成对象
     *
     * @param text     待解析字符串
     * @param clazz    解析目录类
     * @param features 特性
     * @return
     * @author jiayuan.su
     * @date 2019年04月18日
     */
    private static <T> T parseObject(String text, Class<T> clazz, Feature[] features) {
        return JSON.parseObject(text, clazz, features);
    }

    private AssistUtil() {
    }
}
