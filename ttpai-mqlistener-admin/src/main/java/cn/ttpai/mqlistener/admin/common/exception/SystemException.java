package cn.ttpai.mqlistener.admin.common.exception;

/**
 * 系统异常
 *
 * @author jiayuan.su
 */
public class SystemException extends RuntimeException {
    private String errorCode;

    public SystemException(String message) {
        super(message);
        this.errorCode = "";
    }

    public SystemException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause) {
        super(cause);
        this.errorCode = "";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
