package cn.ttpai.mqlistener.core;

import cn.ttpai.mqlistener.core.exception.MqBaseException;
import cn.ttpai.mqlistener.core.util.AssistUtil;

import org.springframework.beans.factory.InitializingBean;

/**
 * 基本配置
 *
 * @author jiayuan.su
 */
public class BaseConfig implements InitializingBean {

    private String projectName;

    private String receivers;

    private String envCode;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (AssistUtil.isBlank(projectName)) {
            throw new MqBaseException("project_name_cannot_null", "项目名不能为空");
        }
        if (AssistUtil.isBlank(receivers)) {
            throw new MqBaseException("receivers_cannot_null", "收件人不能为空");
        }
        if (AssistUtil.isBlank(envCode)) {
            throw new MqBaseException("env_code_cannot_null", "环境代码不能为空");
        }
    }
}
