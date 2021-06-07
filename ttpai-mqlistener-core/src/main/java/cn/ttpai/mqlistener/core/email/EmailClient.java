package cn.ttpai.mqlistener.core.email;

import cn.ttpai.mqlistener.core.email.EmailConfig;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * 自定义SimpleEmail
 *
 * @author jiayuan.su
 */
public class EmailClient {

    /**
     * 邮件基本配置
     */
    private EmailConfig emailBaseConfig;

    public EmailClient(EmailConfig emailBaseConfig) {
        this.emailBaseConfig = emailBaseConfig;
    }

    /**
     * 发送邮件
     *
     * @param emailInfo 邮件正文信息
     * @author jiayuan.su
     * @date 2019年07月02日
     */
    public void sendEmail(EmailInfo emailInfo) throws EmailException {
        HtmlEmail htmlEmail = new HtmlEmail();

        // 基本配置
        htmlEmail.setHostName(this.emailBaseConfig.getHostName());
        htmlEmail.setSmtpPort(this.emailBaseConfig.getSmtpPort());
        htmlEmail.setAuthentication(this.emailBaseConfig.getUsername(), this.emailBaseConfig.getPassword());
        htmlEmail.setSSLOnConnect(this.emailBaseConfig.getSslOnConnect());
        htmlEmail.setFrom(this.emailBaseConfig.getFrom());
        htmlEmail.setCharset(this.emailBaseConfig.getCharset());

        // 邮件正文信息
        htmlEmail.addTo(emailInfo.getReceivers());
        htmlEmail.setSubject(emailInfo.getSubject());
        htmlEmail.setMsg(emailInfo.getContent());

        // 发送
        htmlEmail.send();
    }
}
