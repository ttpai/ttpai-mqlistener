package cn.ttpai.mqlistener.admin.controllers;

import cn.ttpai.mqlistener.admin.common.base.BaseController;
import cn.ttpai.mqlistener.admin.common.constant.MqConstant;
import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.PagelabVO;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.WebResult;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoConditionVO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoVO;
import cn.ttpai.mqlistener.admin.mq.service.MqServiceInfoService;
import cn.ttpai.mqlistener.admin.process.MqServiceInfoProcessService;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.paoding.rose.web.var.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

/**
 * 服务controller
 *
 * @author jiayuan.su
 */
@Path("")
public class ServiceController extends BaseController {

    @Autowired
    private MqServiceInfoProcessService serviceInfoProcessService;
    @Autowired
    private MqServiceInfoService serviceInfoService;

    @Get("")
    public String serviceList(Model model) {
        return "mqListener/serviceList";
    }

    @Get("/servicePage")
    public String servicePage(MqServiceInfoConditionVO conditionVO, @Param("nowPage") Integer nowPage) {
        nowPage = Objects.isNull(nowPage) || nowPage < 1 ? 1 : nowPage;
        Pageable pageInfo = new PageRequest(nowPage - 1, MqConstant.SERVICE_LIST_PAGE_SIZE);

        if (!Objects.isNull(conditionVO.getGroupId()) && conditionVO.getGroupId() < 0) {
            conditionVO.setGroupId(null);
        }

        PagelabVO<MqServiceInfoVO> servicePage = serviceInfoService.selectMqServiceInfoPage(conditionVO, pageInfo);

        return RoseReturn.at(WebResult.success(servicePage).toJson());
    }

    @Get("/getService")
    public String getService(@Param("serviceId") Long serviceId) {
        MqServiceInfoVO serviceInfoVO = serviceInfoService.selectMqServiceInfoById(serviceId);
        if (Objects.isNull(serviceInfoVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SERVICE_NOT_EXIST);
        }

        return RoseReturn.at(WebResult.success(serviceInfoVO).toJson());
    }

    @Post("/editService")
    public String editService(MqServiceInfoVO serviceInfoVO) {
        serviceInfoProcessService.updateService(serviceInfoVO);

        return RoseReturn.at(WebResult.success(null).toJson());
    }

    @Get("/deleteService")
    public String deleteService(@Param("serviceId") Long serviceId) {
        serviceInfoProcessService.deleteService(serviceId);

        return RoseReturn.at(WebResult.success(null).toJson());
    }

    @Get("/syncService")
    public String syncData() {
        serviceInfoProcessService.updateServiceFromZk2Db();
        return RoseReturn.at(WebResult.success(null).toJson());
    }
}
