package cn.ttpai.mqlistener.admin.common.util;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 
 * @Description: 本项目json/VO转换全都使用本工具类
 * @author: simon.JY
 * @date: 2015年7月10日
 * 久兴信息技术(上海)有限公司
 */
public class JsonUtil {
	
	private static final SerializerFeature[] FEATURES = {SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.BrowserCompatible};
	
	/**
	 * 
	 * @Description: toJSONString
	 * @author: simon.JY
	 * @param object vo
	 * @param is2defvalue 表示是否显示值为null的字段
	 * @return
	 * @date: 2015年7月10日 下午12:05:42
	 *
	 */
	public static String toJsonString(Object object, Boolean is2defvalue){
		if(is2defvalue){
			return toJsonString(object, FEATURES);
		}else{
			return toJsonString(object, new SerializerFeature[0]);
		}
	}
	/**
	 * 
	 * @Description: 返回适合前端使用的Json字符串(推荐)
	 * @author: simon.JY
	 * @param object
	 * @return
	 * @date: 2015年7月11日 下午6:39:32
	 *
	 */
	public static String toJsonString(Object object){
		return toJsonString(object, FEATURES);
	}
	
	/**
	 * 
	 * @Description: 调用fastJson（toJSONString）
	 * @author: simon.JY
	 * @param object
	 * @param features
	 * @return
	 * @date: 2015年7月11日 下午6:43:47
	 *
	 */
	public static String toJsonString(Object object, SerializerFeature[] features) {
        return JSON.toJSONString(object, features);
    }
	
	
    public static final <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text, clazz, new Feature[0]);
    }
    
    /**
     * 
     * @Description: 调用fastJson（parseObject）
     * @author: simon.JY
     * @param text
     * @param clazz
     * @param features
     * @return
     * @date: 2015年7月11日 下午6:45:08
     *
     */
    private static final <T> T parseObject(String text, Class<T> clazz, Feature[] features) {
    	return JSON.parseObject(text, clazz, features);
    }
    
    /**
     * 
     * @Description: 调用fastJson（parseArray）
     * @author: simon.JY
     * @param text
     * @param clazz
     * @return
     * @date: 2015年8月13日 下午7:04:27
     *
     */
    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }
    
    
    
    /**
     * 转换JAVA对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        return (T) JSON.toJavaObject(JSON.parseObject(json), clazz);
    }
    
    /**
     * json解析成jsonObject
     * @param json
     * @return
     */
    public static JSONObject parseJsonObject(String json){
        if(isBlank(json)){
            return null;
        }
        return JSON.parseObject(json);
    }
    /**
     * 解析JSON数组
     * @param json
     * @return
     */
    public static JSONArray parseJsonArray(String json){
        if(isBlank(json)){
            return null;
        }
        return JSON.parseArray(json);
    }

	public static String toJson(Object object, SFeature... features) {
		SerializerFeature[] serializerFeatures = SFeature.EMPTY.getFeatures();
		// 只传了一个参数
		if (null != features && features.length == 1) {
			serializerFeatures = features[0].getFeatures();
		}
		// 传了多个参数
		if (null != features && features.length > 1) {
			// 计算总个数
			int featureCount = 0;
			for (SFeature feature : features) {
				featureCount += feature.features.length;
			}
			// 创建总长度数组
			final SerializerFeature[] newFeatures = new SerializerFeature[featureCount];
			// copy 指针
			int point = 0;
			// copy feature 到 newFeatures
			for (SFeature feature : features) {
				System.arraycopy(feature.features, 0, newFeatures, point, feature.features.length);
				point += feature.features.length;
			}
			serializerFeatures = newFeatures;
		}

		return JSON.toJSONString(object, serializerFeatures);
	}

	/**
	 * json 转换成 Java 对象
	 *
	 * @param json  json字符串
	 * @param clazz Java 对象
	 * @param       <T>
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		return toObject(json, (Type) clazz);
	}

	/**
	 * json 转换成 Java 对象（可嵌套）
	 *
	 * @param json          json字符串
	 * @param jsonReference new JsonReference &lt; Map &lt; String, Integer &gt; &gt;() {}; 等方式；new 一个 JsonReference
	 *                          的匿名子类来记录泛型类型
	 * @param               <T> 泛型类型
	 * @return Java 对象
	 */
	public static <T> T toObject(String json, JsonUtil.AbstractJsonReference<T> jsonReference) {
		return toObject(json, jsonReference.getType());
	}

	public static <T> T toObject(String json, Type type) {
		try {
			return JSON.parseObject(json, type);
		} catch (JSONException ex) {
			throw new JSONException(json + " >> error parse to >> " + type.getTypeName(), ex);
		}
	}

	/**
	 * 封装 TypeReference，通过子类来获取泛型类型
	 * <p>
	 *
	 * <pre>
	 * new JsonReference&lt;Map&lt;String, Integer&gt;&gt;() {
	 * };
	 * </pre>
	 */
	public abstract static class AbstractJsonReference<T> extends TypeReference<T> {
	}

    /**
     * 判断是否为空
     * @param json
     * @return
     */
    private static boolean isBlank(String json){
        return (null==json || "".equals(json));
    }

	/**
	 * 封装 fastJson SerializerFeature
	 */
	public enum SFeature {

		/**
		 * 无 SerializerFeature
		 */
		EMPTY,
		/**
		 * 只有 SerializerFeature.BrowserCompatible 避免乱码
		 */
		BROWSER_COMPATIBLE_ONLY(SerializerFeature.BrowserCompatible),
		/**
		 * 如果字段值为 null，保留字段名，值为 null
		 */
		WRITE_NULL_VALUE(SerializerFeature.WriteMapNullValue),
		/**
		 * 格式化输出
		 */
		PRETTY_FORMAT(SerializerFeature.PrettyFormat),
		/**
		 * 如果字段为空，用默认值代替
		 *
		 * @deprecated null 和 "" 的含义是不一样，该序列化方式会改变默认含义，使用 WRITE_NULL_VALUE 代替
		 */
		WRITE_NULL_AS_DEFAULT_VALUE_AND_BROWSER_COMPATIBLE(
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.BrowserCompatible);

		private SerializerFeature[] features = new SerializerFeature[0];

		SFeature(SerializerFeature... features) {
			this.features = features;
		}

		private SerializerFeature[] getFeatures() {
			return features;
		}
	}
}
