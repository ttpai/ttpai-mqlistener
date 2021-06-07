package cn.ttpai.mqlistener.core.email;

/**
 * 邮件信息
 *
 * @author jiayuan.su
 */
public class EmailInfo {

    /**
     * 收件人
     */
    private String[] receivers;

    /**
     * 主题(标题)
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
