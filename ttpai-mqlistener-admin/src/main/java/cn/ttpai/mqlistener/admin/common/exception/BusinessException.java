package cn.ttpai.mqlistener.admin.common.exception;

/**
 * 业务异常
 *
 * @author jiayuan.su
 */
public class BusinessException extends RuntimeException {
    private String errorCode;

    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
