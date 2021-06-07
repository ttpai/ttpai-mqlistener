package cn.ttpai.mqlistener.core.email;

/**
 * 邮件基本配置
 *
 * @author jiayuan.su
 */
public class EmailConfig {

    private String hostName;

    private Integer smtpPort;

    private String username;

    private String password;

    private Boolean sslOnConnect;

    private String from;

    private String charset;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSslOnConnect() {
        return sslOnConnect;
    }

    public void setSslOnConnect(Boolean sslOnConnect) {
        this.sslOnConnect = sslOnConnect;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
