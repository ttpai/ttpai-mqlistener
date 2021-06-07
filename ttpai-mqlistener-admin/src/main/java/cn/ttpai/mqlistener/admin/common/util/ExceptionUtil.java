package cn.ttpai.mqlistener.admin.common.util;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.exception.BusinessException;

/**
 * 异常工具类
 * <p>
 * @author jiayuan.su
 */
public final class ExceptionUtil {

    /**
      * 构建BusinessException实例
      *
      * @param errorInfoEnum 异常信息枚举
      * @return BusinessException 业务异常
      * @author jiayuan.su
      * @date 2020年07月24日
      */
    public static BusinessException buildBusinessException(ErrorInfoEnum errorInfoEnum) {
        return new BusinessException(errorInfoEnum.getCode(), errorInfoEnum.getMessage());
    }

    private ExceptionUtil() {}
}
