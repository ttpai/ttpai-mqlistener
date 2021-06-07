package cn.ttpai.mqlistener.admin.common.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 字符串填充工具类
 *
 * @author jiayuan.su
 */
public final class StrFillUtil {
    private static final String PLACE_HOLDER_PREFIX = "{";
    private static final String PLACE_HOLDER_SUFFIX = "}";
    private static final String VALUE_SEPARATOR = ":";
    private static final String SIMPLE_PREFIX = "{";

    public static Builder item(String name, Object value) {
        return new Builder(name, value);
    }

    public static final class Builder {
        private Map<String, Object> dataMap;

        public Builder(String name, Object value) {
            this.dataMap = new HashMap<>();
            this.dataMap.put(name, value);
        }

        public Builder item(String name, Object value) {
            this.dataMap.put(name, value);
            return this;
        }

        public String target(String targetStr) {
            return target(targetStr, false);
        }

        public String target(String targetStr, boolean ignoreNull) {
            return parseStringValue(targetStr, new PlaceholderResolver() {
                @Override
                public String resolvePlaceholder(String placeholderName) {
                    Object placeholderValue = dataMap.get(placeholderName);
                    return null == placeholderValue ? null : placeholderValue.toString();
                }
            }, new HashSet<String>(), ignoreNull);
        }

        private static String parseStringValue(String strVal, PlaceholderResolver placeholderResolver
                , Set<String> visitedPlaceholders, Boolean ignoreUnresolvablePlaceholders) {
            StringBuilder result = new StringBuilder(strVal);

            int startIndex = strVal.indexOf(PLACE_HOLDER_PREFIX);
            while (startIndex != -1) {
                int endIndex = findPlaceholderEndIndex(result, startIndex);
                if (endIndex != -1) {
                    String placeholder = result.substring(startIndex + PLACE_HOLDER_PREFIX.length(), endIndex);
                    String originalPlaceholder = placeholder;
                    if (!visitedPlaceholders.add(originalPlaceholder)) {
                        throw new IllegalArgumentException(
                                "Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                    }
                    // Recursive invocation, parsing placeholders contained in the placeholder key.
                    placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders, ignoreUnresolvablePlaceholders);
                    // Now obtain the value for the fully resolved key...
                    String propVal = placeholderResolver.resolvePlaceholder(placeholder);
                    if (propVal == null && VALUE_SEPARATOR != null) {
                        int separatorIndex = placeholder.indexOf(VALUE_SEPARATOR);
                        if (separatorIndex != -1) {
                            String actualPlaceholder = placeholder.substring(0, separatorIndex);
                            String defaultValue = placeholder.substring(separatorIndex + VALUE_SEPARATOR.length());
                            propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
                            if (propVal == null) {
                                propVal = defaultValue;
                            }
                        }
                    }
                    if (propVal != null) {
                        // Recursive invocation, parsing placeholders contained in the
                        // previously resolved placeholder value.
                        propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders, ignoreUnresolvablePlaceholders);
                        result.replace(startIndex, endIndex + PLACE_HOLDER_SUFFIX.length(), propVal);
                        startIndex = result.indexOf(PLACE_HOLDER_PREFIX, startIndex + propVal.length());
                    } else if (ignoreUnresolvablePlaceholders) {
                        // Proceed with unprocessed value.
                        startIndex = result.indexOf(PLACE_HOLDER_PREFIX, endIndex + PLACE_HOLDER_SUFFIX.length());
                    } else {
                        throw new IllegalArgumentException("Could not resolve placeholder '" +
                                placeholder + "'" + " in string value \"" + strVal + "\"");
                    }
                    visitedPlaceholders.remove(originalPlaceholder);
                } else {
                    startIndex = -1;
                }
            }

            return result.toString();
        }

        private static int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
            int index = startIndex + PLACE_HOLDER_PREFIX.length();
            int withinNestedPlaceholder = 0;
            while (index < buf.length()) {
                if (StringUtils.substringMatch(buf, index, PLACE_HOLDER_SUFFIX)) {
                    if (withinNestedPlaceholder > 0) {
                        withinNestedPlaceholder--;
                        index = index + PLACE_HOLDER_SUFFIX.length();
                    } else {
                        return index;
                    }
                } else if (StringUtils.substringMatch(buf, index, SIMPLE_PREFIX)) {
                    withinNestedPlaceholder++;
                    index = index + SIMPLE_PREFIX.length();
                } else {
                    index++;
                }
            }
            return -1;
        }
    }

    private interface PlaceholderResolver {
        String resolvePlaceholder(String placeholderName);
    }

    private StrFillUtil() {}
}
