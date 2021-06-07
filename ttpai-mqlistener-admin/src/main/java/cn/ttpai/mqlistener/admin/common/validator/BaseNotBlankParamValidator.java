package cn.ttpai.mqlistener.admin.common.validator;

import cn.ttpai.mqlistener.admin.common.annotation.NotBlank;
import cn.ttpai.mqlistener.admin.common.util.AjaxUtil;
import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.common.util.ResultJson;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.ParamValidator;
import net.paoding.rose.web.paramresolver.ParamMetaData;

import org.springframework.validation.Errors;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础not blank参数校验器
 *
 * @author jiayuan.su
 */
public class BaseNotBlankParamValidator implements ParamValidator {

    @Override
    public boolean supports(ParamMetaData metaData) {
        return null != metaData.getAnnotation(NotBlank.class);
    }

    @Override
    public Object validate(ParamMetaData metaData, Invocation inv, Object target, Errors errors) {
        // 1.参数值
        String paramName = metaData.getParamName();
        String paramValue = inv.getParameter(paramName);

        // 2.校验通过时直接返回
        if (StringUtil.isNotBlank(paramValue) && null != target) {
            return null;
        }

        // 3.错误信息
        String errorMsg = StringUtil.isBlank(paramValue) ? "参数不能为空" : "参数格式错误";

        // 4.返回错误信息
        final HttpServletRequest request = inv.getRequest();
        if(AjaxUtil.isAjax(request) || "ajax".equals(request.getParameter("ajax"))){
            ResultJson resultJson = new ResultJson();
            resultJson.setCode("600");
            resultJson.setMessage(errorMsg);

            String callback = request.getParameter("callback");
            if (StringUtil.isNotBlank(callback)) {
                return String.format("%s(%s)", callback, JsonUtil.toJsonString(resultJson));
            }

            return RoseReturn.json(JsonUtil.toJsonString(resultJson));
        }

        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("errorResult", Collections.emptyList());

        return "error";
    }
}
