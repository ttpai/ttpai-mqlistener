package cn.ttpai.mqlistener.core.exception;

/**
 * mq base 异常类
 *
 * @author jiayuan.su
 */
public class MqBaseException extends RuntimeException {

    private String errorCode;

    public MqBaseException(String errorCode, String defaultMessage) {
        super(defaultMessage);
        this.setErrorCode(errorCode);
    }

    public MqBaseException(String errorCode, String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
        this.setErrorCode(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
