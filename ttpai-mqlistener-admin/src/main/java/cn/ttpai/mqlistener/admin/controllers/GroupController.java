package cn.ttpai.mqlistener.admin.controllers;

import cn.ttpai.mqlistener.admin.common.base.BaseController;
import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.enums.YesOrNoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;
import cn.ttpai.mqlistener.admin.common.util.WebResult;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceGroupVO;
import cn.ttpai.mqlistener.admin.mq.service.MqServiceGroupService;
import cn.ttpai.mqlistener.admin.process.MqServiceInfoProcessService;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.paoding.rose.web.var.Model;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * 组controller
 *
 * @author jiayuan.su
 */
@Path("")
public class GroupController extends BaseController {

    @Autowired
    private MqServiceGroupService serviceGroupService;
    @Autowired
    private MqServiceInfoProcessService serviceInfoProcessService;

    @Get("/groupList")
    public String groupList(Model model) {
        return "mqListener/groupList";
    }

    @Get("/groupListData")
    public String groupListData() {
        List<MqServiceGroupVO> serviceGroupList = serviceGroupService.selectMqServiceGroupList();

        return RoseReturn.at(WebResult.success(serviceGroupList).toJson());
    }

    @Get("/getGroup")
    public String getGroup(@Param("groupId") Long groupId) {
        MqServiceGroupVO serviceGroupVO = serviceGroupService.selectServiceGroup(groupId);
        if (Objects.isNull(serviceGroupVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_NOT_EXIST);
        }

        return RoseReturn.at(WebResult.success(serviceGroupVO).toJson());
    }

    @Post("/editGroup")
    public String editGroup(MqServiceGroupVO serviceGroupVO) {
        checkAndFillDefault(serviceGroupVO);

        if (Objects.isNull(serviceGroupVO.getId())) {
            serviceGroupService.saveServiceGroup(serviceGroupVO);
        } else {
            serviceInfoProcessService.updateServiceGroup(serviceGroupVO);
        }

        return RoseReturn.at(WebResult.success(null).toJson());
    }

    @Get("/deleteGroup")
    public String deleteGroup(@Param("groupId") Long groupId) {
        serviceInfoProcessService.deleteGroup(groupId);

        return RoseReturn.at(WebResult.success(null).toJson());
    }

    /**
     * 检验并设置默认值
     */
    private void checkAndFillDefault(MqServiceGroupVO serviceGroupVO) {
        if (StringUtil.isBlank(serviceGroupVO.getGroupName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_NAME_IS_NULL);
        }
        if (StringUtil.isBlank(serviceGroupVO.getGroupDesc())) {
            serviceGroupVO.setGroupDesc("");
        }
        serviceGroupVO.setDeleted(YesOrNoEnum.NO.getCode());
    }
}
