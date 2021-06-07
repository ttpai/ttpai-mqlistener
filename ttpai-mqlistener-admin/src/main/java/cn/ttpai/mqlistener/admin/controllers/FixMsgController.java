package cn.ttpai.mqlistener.admin.controllers;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.WebResult;
import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgVO;
import cn.ttpai.mqlistener.admin.mq.model.MqFixVO;
import cn.ttpai.mqlistener.admin.mq.service.MqFixMsgService;
import cn.ttpai.mqlistener.admin.mq.service.MqFixService;
import cn.ttpai.mqlistener.admin.process.MqFixMsgProcessService;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitSendDataVO;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Objects;

/**
 * 修复消息controller
 * <p>
 * @author jiayuan.su
 */
@Path("")
public class FixMsgController {

    @Autowired
    private MqFixService fixService;

    @Autowired
    private MqFixMsgService fixMsgService;

    @Autowired
    private MqFixMsgProcessService fixMsgProcessService;

    @Get("/fixList")
    public String fixList() {
        return "mqListener/fixList";
    }

    @Get("/fixListData")
    public String fixListData(MqFixVO queryVO, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize) {
        Page<MqFixVO> fixPage = fixService.selectFixPage(queryVO, new PageRequest(pageNum - 1, pageSize));

        return RoseReturn.at(WebResult.success(fixPage).toJson());
    }

    @Get("/getFix4Edit")
    public String getFix4Edit(@Param("fixId") Long fixId) {
        MqFixVO fixVO = fixService.selectFix4Edit(fixId);

        return RoseReturn.at(WebResult.success(fixVO).toJson());
    }

    @Get("/getFix")
    public String getFix(@Param("fixId") Long fixId) {
        MqFixVO fixVO = fixService.selectFix(fixId);

        return RoseReturn.at(WebResult.success(fixVO).toJson());
    }

    @Post("/addOrEditFix")
    public String addOrEditFix(MqFixVO addOrEditVO) {
        if (Objects.isNull(addOrEditVO.getId())) {
            fixService.saveFix(addOrEditVO);
        } else {
            fixService.updateFix(addOrEditVO);
        }

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Get("/fixSummary")
    public String fixSummary(@Param("fixId") Long fixId) {
        MqFixVO fixVO = fixMsgService.selectFixSummary(fixId);

        return RoseReturn.at(WebResult.success(fixVO).toJson());
    }

    @Post("/endFix")
    public String endFix(@Param("fixId") Long fixId, @Param("remark") String remark) {
        fixService.updateFix2End(fixId, remark);

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Get("/fixMsgList")
    public String fixMsgList(@Param("fixId") Long fixId) {
        if (Objects.isNull(fixId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }

        return "mqlistener/fixMsgList";
    }

    @Get("/fixMsgListData")
    public String fixMsgListData(MqFixMsgVO queryVO, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize) {
        Page<MqFixMsgVO> fixMsgPage = fixMsgService.selectFixMsgPage(queryVO, new PageRequest(pageNum - 1, pageSize));

        return RoseReturn.at(WebResult.success(fixMsgPage).toJson());
    }

    @Post("/addFixMsg")
    public String addFixMsg(MqFixMsgVO saveVO) {
        fixMsgProcessService.addFixMsg(saveVO);

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Get("/getFixMsg")
    public String getFixMsg(@Param("fixMsgId") Long fixMsgId) {
        MqFixMsgVO fixMsgVO = fixMsgService.selectFixMsg4Edit(fixMsgId, true);

        return RoseReturn.at(WebResult.success(fixMsgVO).toJson());
    }

    @Post("/editFixMsg")
    public String editFixMsg(MqFixMsgVO updateVO) {
        fixMsgProcessService.updateFixMsg(updateVO);

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Post("/deleteFixMsg")
    public String deleteFixMsg(@Param("fixId") Long fixId, @Param("fixMsgId") Long fixMsgId) {
        fixMsgProcessService.deleteFixMsgs(fixId, Arrays.asList(fixMsgId));

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Post("/sendFixMsg")
    public String sendFixMsg(RabbitSendDataVO sendDataVO) {
        fixMsgProcessService.sendFixMsg(sendDataVO, Objects.equals(1, sendDataVO.getBatched()));

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Post("/resetFixMsg")
    public String resetFixMsg(@Param("fixMsgId") Long fixMsgId) {
        boolean fixIsEnd = fixMsgProcessService.selectFixIsEnd(fixMsgId);
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        fixMsgService.updateFixMsgStatus2Wait(fixMsgId);

        return RoseReturn.at(WebResult.success("").toJson());
    }

    @Post("/setFixMsgSuccess")
    public String setFixMsgSuccess(@Param("fixMsgId") Long fixMsgId, @Param("fixRemark") String fixRemark) {
        boolean fixIsEnd = fixMsgProcessService.selectFixIsEnd(fixMsgId);
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        fixMsgService.updateFixMsgStatus2Success(fixMsgId, fixRemark);

        return RoseReturn.at(WebResult.success("").toJson());
    }
}
