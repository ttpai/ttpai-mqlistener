package cn.ttpai.mqlistener.starter;

/**
 * mq listener 配置
 *
 * @author jiayuan.su
 */
public class MqListenerProperties {
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 告警邮件收件人
     */
    private String exceptionEmailReceivers;
    /**
     * 环境code: 0-开发, 1-测试, 2-模拟, 3-正式
     */
    private String envCode;

    /**
     * zk重试参数
     */
    private int zooBaseSleepTimeMs = 1000;
    /**
     * zk重试参数
     */
    private int zooMaxRetries = 3;
    /**
     * zk地址
     */
    private String zooAddress;
    /**
     * zk数据根路径
     */
    private String zooRootPath;

    /**
     * 告警邮箱账号配置
     */
    private String emailClientHost;
    /**
     * 告警邮箱账号配置
     */
    private Integer emailClientPort;
    /**
     * 告警邮箱账号配置
     */
    private String emailClientUsername;
    /**
     * 告警邮箱账号配置
     */
    private String emailClientPassword;
    /**
     * 告警邮箱账号配置
     */
    private Boolean emailClientUsessl;
    /**
     * 告警邮箱账号配置
     */
    private String emailClientFrom;
    /**
     * 告警邮箱账号配置
     */
    private String emailClientCharset;

    /**
     * app rabbitmq 主机名
     */
    private String rabbitmqAppHost;
    /**
     * app rabbitmq 端口号
     */
    private Integer rabbitmqAppPort;
    /**
     * app rabbitmq 用户名
     */
    private String rabbitmqAppUsername;
    /**
     * app rabbitmq 密码
     */
    private String rabbitmqAppPassword;
    /**
     * soa rabbitmq 主机名
     */
    private String rabbitmqSoaHost;
    /**
     * soa rabbitmq 端口号
     */
    private Integer rabbitmqSoaPort;
    /**
     * soa rabbitmq 用户名
     */
    private String rabbitmqSoaUsername;
    /**
     * soa rabbitmq 密码
     */
    private String rabbitmqSoaPassword;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExceptionEmailReceivers() {
        return exceptionEmailReceivers;
    }

    public void setExceptionEmailReceivers(String exceptionEmailReceivers) {
        this.exceptionEmailReceivers = exceptionEmailReceivers;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public int getZooBaseSleepTimeMs() {
        return zooBaseSleepTimeMs;
    }

    public void setZooBaseSleepTimeMs(int zooBaseSleepTimeMs) {
        this.zooBaseSleepTimeMs = zooBaseSleepTimeMs;
    }

    public int getZooMaxRetries() {
        return zooMaxRetries;
    }

    public void setZooMaxRetries(int zooMaxRetries) {
        this.zooMaxRetries = zooMaxRetries;
    }

    public String getZooAddress() {
        return zooAddress;
    }

    public void setZooAddress(String zooAddress) {
        this.zooAddress = zooAddress;
    }

    public String getZooRootPath() {
        return zooRootPath;
    }

    public void setZooRootPath(String zooRootPath) {
        this.zooRootPath = zooRootPath;
    }

    public String getEmailClientHost() {
        return emailClientHost;
    }

    public void setEmailClientHost(String emailClientHost) {
        this.emailClientHost = emailClientHost;
    }

    public Integer getEmailClientPort() {
        return emailClientPort;
    }

    public void setEmailClientPort(Integer emailClientPort) {
        this.emailClientPort = emailClientPort;
    }

    public String getEmailClientUsername() {
        return emailClientUsername;
    }

    public void setEmailClientUsername(String emailClientUsername) {
        this.emailClientUsername = emailClientUsername;
    }

    public String getEmailClientPassword() {
        return emailClientPassword;
    }

    public void setEmailClientPassword(String emailClientPassword) {
        this.emailClientPassword = emailClientPassword;
    }

    public Boolean getEmailClientUsessl() {
        return emailClientUsessl;
    }

    public void setEmailClientUsessl(Boolean emailClientUsessl) {
        this.emailClientUsessl = emailClientUsessl;
    }

    public String getEmailClientFrom() {
        return emailClientFrom;
    }

    public void setEmailClientFrom(String emailClientFrom) {
        this.emailClientFrom = emailClientFrom;
    }

    public String getEmailClientCharset() {
        return emailClientCharset;
    }

    public void setEmailClientCharset(String emailClientCharset) {
        this.emailClientCharset = emailClientCharset;
    }

    public String getRabbitmqAppHost() {
        return rabbitmqAppHost;
    }

    public void setRabbitmqAppHost(String rabbitmqAppHost) {
        this.rabbitmqAppHost = rabbitmqAppHost;
    }

    public Integer getRabbitmqAppPort() {
        return rabbitmqAppPort;
    }

    public void setRabbitmqAppPort(Integer rabbitmqAppPort) {
        this.rabbitmqAppPort = rabbitmqAppPort;
    }

    public String getRabbitmqAppUsername() {
        return rabbitmqAppUsername;
    }

    public void setRabbitmqAppUsername(String rabbitmqAppUsername) {
        this.rabbitmqAppUsername = rabbitmqAppUsername;
    }

    public String getRabbitmqAppPassword() {
        return rabbitmqAppPassword;
    }

    public void setRabbitmqAppPassword(String rabbitmqAppPassword) {
        this.rabbitmqAppPassword = rabbitmqAppPassword;
    }

    public String getRabbitmqSoaHost() {
        return rabbitmqSoaHost;
    }

    public void setRabbitmqSoaHost(String rabbitmqSoaHost) {
        this.rabbitmqSoaHost = rabbitmqSoaHost;
    }

    public Integer getRabbitmqSoaPort() {
        return rabbitmqSoaPort;
    }

    public void setRabbitmqSoaPort(Integer rabbitmqSoaPort) {
        this.rabbitmqSoaPort = rabbitmqSoaPort;
    }

    public String getRabbitmqSoaUsername() {
        return rabbitmqSoaUsername;
    }

    public void setRabbitmqSoaUsername(String rabbitmqSoaUsername) {
        this.rabbitmqSoaUsername = rabbitmqSoaUsername;
    }

    public String getRabbitmqSoaPassword() {
        return rabbitmqSoaPassword;
    }

    public void setRabbitmqSoaPassword(String rabbitmqSoaPassword) {
        this.rabbitmqSoaPassword = rabbitmqSoaPassword;
    }
}
