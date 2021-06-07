package cn.ttpai.mqlistener.admin.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串工具类
 *
 * @author jiayuan.su
 */
public final class StringUtil {

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
     * 获取字符串的值
     *
     * @param value 原值
     * @param defaultValue 默认值
     * @return String 返回值
     * @author jiayuan.su
     * @date 2019年04月15日
     */
    public static String avoidBlank(String value, String defaultValue) {
        return isNotBlank(value) ? value : defaultValue;
    }

    private StringUtil() {}
}
