package cn.ttpai.mqlistener.admin.common.component.property;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.StandardEnvironment;

/**
 * 属性文件公共类(PropertySourcesPlaceholderConfigurer)
 * <p>
 * 依赖项： <b style="color:red">org.springframework:spring-beans</b>
 *
 * @author Kail
 */
public class PropertiesUtils extends PropertySourcesPlaceholderConfigurer {

    private static ConfigurableEnvironment ENV;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1001;
    }

    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);
        if (environment instanceof ConfigurableEnvironment) {
            ENV = (ConfigurableEnvironment) environment;
        } else {
            ENV = new StandardEnvironment();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);

        PropertySources propertySources = super.getAppliedPropertySources();
        ENV.getPropertySources().addLast(propertySources.get(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME));
    }

    /**
     * 获取属性值，获取不到是 null
     *
     * @param key 键
     * @return 属性值
     */
    public static String getStringDefaultNull(String key) {
        return getString(key, null);
    }

    /**
     * 获取属性值，获取不到是 空字符串
     *
     * @param key 键
     * @return 属性值
     */
    public static String getStringDefaultBlank(String key) {
        return getString(key, "");
    }

    /**
     * 获取属性值
     *
     * @param key          键
     * @param defaultValue 如果key不存在时的默认值
     * @return 属性值
     */
    public static String getString(String key, String defaultValue) {
        return ENV.getProperty(key, defaultValue);
    }

    /**
     * 获取属性值,获取不到是 null
     *
     * @param key 键
     * @return 属性值
     */
    public static Integer getIntegerDefaultNull(String key) {
        return getInteger(key, null);
    }

    /**
     * 获取属性值
     *
     * @param key          键
     * @param defaultValue 如果key不存在时的默认值
     * @return 属性值
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        String value = getStringDefaultBlank(key);
        if (value.matches(RegexConstant.RE_NUM)) {
            return Integer.valueOf(value);
        } else {
            return defaultValue;
        }
    }
}
