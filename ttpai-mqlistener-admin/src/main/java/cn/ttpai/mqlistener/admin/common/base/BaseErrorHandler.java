package cn.ttpai.mqlistener.admin.common.base;


import cn.ttpai.mqlistener.admin.common.exception.BusinessException;
import cn.ttpai.mqlistener.admin.common.util.AjaxUtil;
import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.common.util.ResultJson;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;

import net.paoding.rose.web.ControllerErrorHandler;
import net.paoding.rose.web.Invocation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用异常处理器
 *
 * @author jiayuan.su
 */
public class BaseErrorHandler implements ControllerErrorHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EXCEPTION_ATTR = "javax.servlet.error.exception";
    private static final String EXCEPTION_TYPE_ATTR = "javax.servlet.error.exception_type";
    private static final String STATUS_CODE_ATTR = "javax.servlet.error.status_code";
    private static final String APPACHE_TOMCAT = "Apache Tomcat";

    @Override
    public Object onError(Invocation inv, Throwable ex) throws Throwable {
        // 1.获取request
        final HttpServletRequest request = inv.getRequest();

        // 2.打印异常信息
        String errorMsg = StringUtil.avoidBlank(extractErrorMsg(ex), "无");
        String causeMsg = StringUtil.avoidBlank(extractCauseMsg(ex), "无");
        Class<?> controllerClazz = inv.getControllerClass();
        String methodName = inv.getMethod().getName();
        String paramValue = StringUtil.avoidBlank(extractParamValue(request), "无");

        String logMsg = String.format("[errorMsg: %s][causeMsg: %s][controller: %s][method: %s][param: %s]",
                errorMsg, causeMsg, controllerClazz, methodName, paramValue);
        logger.error(logMsg);
        logger.error("请求异常:", ex);

        // 3.处理tomcat容器自带异常报告
        String serverInfo = inv.getApplicationContext().getServletContext().getServerInfo();
        Object state = request.getAttribute(STATUS_CODE_ATTR);
        if (StringUtils.containsIgnoreCase(serverInfo, APPACHE_TOMCAT) && null != state && "200".equals(state.toString())) {
            request.removeAttribute(EXCEPTION_TYPE_ATTR);
            request.removeAttribute(EXCEPTION_ATTR);
        }

        // 4.错误信息
        String webErrorCode = ex instanceof BusinessException ? ((BusinessException) ex).getErrorCode() : "600";
        String webErrorMsg = ex instanceof BusinessException ? ex.getMessage() : "系统异常";

        // 4.返回结果给前端
        if(AjaxUtil.isAjax(request) || "ajax".equals(request.getParameter("ajax"))){
            ResultJson resultJson = new ResultJson();
            resultJson.setCode(webErrorCode);
            resultJson.setMessage(webErrorMsg);

            String callback = request.getParameter("callback");
            if (StringUtil.isNotBlank(callback)) {
                return String.format("%s(%s)", callback, JsonUtil.toJsonString(resultJson));
            }

            return RoseReturn.json(JsonUtil.toJsonString(resultJson));
        }

        request.setAttribute("errorMsg", webErrorMsg);
        request.setAttribute("errorResult", ex.getStackTrace());

        return "error";
    }

    /**
     * 提取异常消息
     */
    private String extractErrorMsg(Throwable ex) {
        return ex instanceof BusinessException ? ((BusinessException) ex).getErrorCode() + "," + ex.getMessage() : ex.getMessage();
    }

    /**
     * 提取异常源的消息
     */
    private String extractCauseMsg(Throwable ex) {
        Throwable cause = ex.getCause();
        return null != cause ? cause.getMessage() : "";
    }

    /**
     * 提取请求参数
     */
    private String extractParamValue(HttpServletRequest request) {
        Enumeration<String> parameterNamesEnumeration = request.getParameterNames();

        StringBuilder resultBuilder = new StringBuilder();
        boolean appendConnector = false;
        while (parameterNamesEnumeration.hasMoreElements()) {
            if (!appendConnector) {
                appendConnector = true;
            } else {
                resultBuilder.append("&");
            }
            String parameterName = parameterNamesEnumeration.nextElement();
            String parameterNameValue = request.getParameter(parameterName);
            resultBuilder.append(parameterName).append("=").append(parameterNameValue);
        }

        return resultBuilder.toString();
    }
}