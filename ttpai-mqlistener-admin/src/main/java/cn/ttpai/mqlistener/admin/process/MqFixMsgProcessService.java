package cn.ttpai.mqlistener.admin.process;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.enums.YesOrNoEnum;
import cn.ttpai.mqlistener.admin.common.exception.BusinessException;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.mq.enums.MQAddFixMsgTypeEnum;
import cn.ttpai.mqlistener.admin.mq.enums.MQFixMsgStatusEnum;
import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgTextVO;
import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgVO;
import cn.ttpai.mqlistener.admin.mq.service.MqFixMsgService;
import cn.ttpai.mqlistener.admin.mq.service.MqFixService;
import cn.ttpai.mqlistener.admin.rabbit.enums.RabbitServerTypeEnum;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitBasisInfoVO;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitGetMsgVO;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitSendDataVO;
import cn.ttpai.mqlistener.admin.rabbit.service.RabbitService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MQ修复消息processService
 *
 * @author jiayuan.su
 */
@Service
public class MqFixMsgProcessService {
    private static final Logger logger = LoggerFactory.getLogger(MqFixMsgProcessService.class);

    @Autowired
    private MqFixService fixService;
    @Autowired
    private MqFixMsgService fixMsgService;
    @Autowired
    private RabbitService rabbitService;

    public void addFixMsg(MqFixMsgVO saveVO) {
        MQAddFixMsgTypeEnum addFixMsgTypeEnum = MQAddFixMsgTypeEnum.getEnum(saveVO.getAddFixMsgType());
        if (Objects.isNull(addFixMsgTypeEnum)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.ADD_FIX_MSG_TYPE_WRONG);
        }

        switch (addFixMsgTypeEnum) {
            case UI_INPUT:
                this.saveFixMsg(saveVO);
                break;
            case TEXT_CONTENT:
                this.saveFixMsgList(saveVO);
                break;
            case REMOTE_GET:
                this.remoteGetAndSaveFixMsg(saveVO);
                break;
            default:
                throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.ADD_FIX_MSG_TYPE_WRONG);
        }
    }

    /**
     * 更新修复消息数据
     *
     * @param paramUpdateVO 待更新的修复数据
     * @author jiayuan.su
     * @date 2020年09月09日
     */
    public void updateFixMsg(MqFixMsgVO paramUpdateVO) {
        if (Objects.isNull(paramUpdateVO.getId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }
        if (Objects.isNull(paramUpdateVO.getOrderNum())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.ORDER_NUM_ILLEGAL);
        }
        if (StringUtils.isBlank(paramUpdateVO.getMsgText())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_TEXT_IS_BLANK);
        }

        MqFixMsgVO fixMsgVO = fixMsgService.selectFixMsg4Edit(paramUpdateVO.getId(), false);

        // 修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(fixMsgVO.getFixId());
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        // 排序
        if (!Objects.equals(paramUpdateVO.getOrderNum(), fixMsgVO.getOrderNum())) {
            fixMsgService.updateOrder(fixMsgVO.getFixId(), paramUpdateVO.getOrderNum());
        }

        // 更新消息信息
        fixMsgService.updateFixMsg(paramUpdateVO.getId(), paramUpdateVO.getOrderNum());
        fixMsgService.updateFixMsgText(fixMsgVO.getMsgTextId(), paramUpdateVO.getMsgText());
    }

    /**
     * 删除修复消息数据
     *
     * @param fixId 修复ID
     * @param idList 修复消息ID
     * @author jiayuan.su
     * @date 2020年09月09日
     */
    public void deleteFixMsgs(Long fixId, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }

        // 修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(fixId);
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        fixMsgService.deleteFixMsgByIdList(fixId, idList);
    }

    /**
      * 发送修复消息并更新修复消息状态
      *
      * @param sendDataVO 发送数据VO
      * @author jiayuan.su
      * @date 2020年09月14日
      */
    public void sendFixMsg(RabbitSendDataVO sendDataVO, boolean isThrow) {
        // 校验数据
        checkSendDataVO(sendDataVO);

        // 消息数据
        MqFixMsgVO dbFixMsgVO = fixMsgService.selectFixMsg(sendDataVO.getFixMsgId(), true);
        if (Objects.isNull(dbFixMsgVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_DATA_NOT_EXIST);
        }
        if (!Objects.equals(MQFixMsgStatusEnum.WAIT_FIX.getStatus(), dbFixMsgVO.getFixMsgStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_NOT_WAIT_FIX_STATUS);
        }

        // 修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(dbFixMsgVO.getFixId());
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        // 消息文本
        sendDataVO.setMsgText(dbFixMsgVO.getMsgText());

        // 获取认证信息
        RabbitBasisInfoVO authVO = rabbitService.getBasisInfoVO(sendDataVO.getServerType());

        // ------------------- 开始修复 -------------------
        // 更新修复状态为修复中
        fixMsgService.updateFixMsgStatus(sendDataVO.getFixMsgId(), MQFixMsgStatusEnum.IN_FIX.getStatus(), "正在进行修复...");

        // 发送数据
        try {
            rabbitService.sendMsg2Queue(sendDataVO, authVO);
        } catch (Exception e) {
            logger.error("发送消息到队列异常. 参数: fixMsgId={}, serverType={}, vhost={}, queueName={}. 异常详情: ",
                    sendDataVO.getFixMsgId(), sendDataVO.getServerType(), sendDataVO.getVhost(), sendDataVO.getQueueName(), e);
            String fixRemark = e instanceof BusinessException ? e.getMessage() : "发送消息到队列异常";
            updateFixMsgStatus(sendDataVO.getFixMsgId(), MQFixMsgStatusEnum.FIX_ERROR.getStatus(), fixRemark, false);
            if (isThrow) {
                String errorCode = e instanceof BusinessException ? ((BusinessException) e).getErrorCode() : ErrorInfoEnum.SYSTEM_ERROR.getCode();
                String errorMessage = e instanceof BusinessException ? e.getMessage() : ErrorInfoEnum.SYSTEM_ERROR.getMessage();
                throw new BusinessException(errorCode, errorMessage);
            }
            return;
        }

        // 更新为成功状态
        updateFixMsgStatus(sendDataVO.getFixMsgId(), MQFixMsgStatusEnum.FIX_SUCCESS.getStatus(), "发送消息且更新状态成功", isThrow);
    }

    /**
      * 查询修复是否结束
      *
      * @param fixMsgId 修复消息ID
      * @return boolean true-是, false-否
      * @author jiayuan.su
      * @date 2020年09月17日
      */
    public boolean selectFixIsEnd(Long fixMsgId) {
        if (Objects.isNull(fixMsgId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }
        MqFixMsgVO fixMsgVO = fixMsgService.selectFixMsg(fixMsgId, false);
        if (Objects.isNull(fixMsgVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_DATA_NOT_EXIST);
        }

        return fixService.selectFixIsEnd(fixMsgVO.getFixId());
    }

    /**
     * 保存修复消息数据
     */
    private void saveFixMsg(MqFixMsgVO paramSaveVO) {
        if (Objects.isNull(paramSaveVO.getFixId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }
        if (StringUtils.isBlank(paramSaveVO.getMsgText())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_TEXT_IS_BLANK);
        }

        // -----修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(paramSaveVO.getFixId());
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        // -----保存数据
        // 保存消息文本
        MqFixMsgTextVO msgTextVO = new MqFixMsgTextVO();
        msgTextVO.setMsgText(paramSaveVO.getMsgText());
        Long msgTextId = fixMsgService.saveFixMsgText(msgTextVO);

        // 保存消息信息
        Integer maxOrderNum = fixMsgService.selectMaxOrderNum(paramSaveVO.getFixId());

        MqFixMsgVO saveVO = new MqFixMsgVO();
        saveVO.setFixId(paramSaveVO.getFixId());
        saveVO.setMsgTextId(msgTextId);
        saveVO.setOrderNum(Objects.isNull(maxOrderNum) ? 1 : maxOrderNum + 1);
        saveVO.setFixMsgStatus(MQFixMsgStatusEnum.WAIT_FIX.getStatus());
        saveVO.setFixRemark("");
        saveVO.setDeleted(YesOrNoEnum.NO.getCode());

        fixMsgService.saveFixMsg(saveVO);
    }

    /**
     * 校验要发送的修复消息数据
     */
    private void checkSendDataVO(RabbitSendDataVO sendDataVO) {
        if (RabbitServerTypeEnum.isNotExist(sendDataVO.getServerType())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.RABBIT_SERVER_TYPE_WRONG);
        }
        if (StringUtils.isBlank(sendDataVO.getVhost())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.VIRTUAL_HOST_IS_BLANK);
        }
        if (StringUtils.isBlank(sendDataVO.getQueueName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.QUEUE_NAME_IS_BLANK);
        }
        if (Objects.isNull(sendDataVO.getFixMsgId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }
    }

    /**
     * 更新修复消息的状态
     */
    private void updateFixMsgStatus(Long fixMsgId, Integer fixMsgStatus, String fixRemark, boolean isThrow) {
        try {
            fixMsgService.updateFixMsgStatus(fixMsgId, fixMsgStatus, fixRemark);
        } catch (Exception e) {
            logger.error("更新修复消息的状态失败. 参数: fixMsgId={}, fixMsgStatus={}, fixRemark={}. 异常详情: ",
                    fixMsgId, fixMsgStatus, fixRemark, e);
            if (isThrow) {
                String errorCode = e instanceof BusinessException ? ((BusinessException) e).getErrorCode() : ErrorInfoEnum.SYSTEM_ERROR.getCode();
                String errorMessage = e instanceof BusinessException ? e.getMessage() : ErrorInfoEnum.SYSTEM_ERROR.getMessage();
                throw new BusinessException(errorCode, errorMessage);
            }
        }
    }

    /**
     * 远程获取消息并保存
     */
    private void remoteGetAndSaveFixMsg(MqFixMsgVO paramSaveVO) {
        // -----参数校验
        if (Objects.isNull(paramSaveVO.getFixId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }
        if (RabbitServerTypeEnum.isNotExist(paramSaveVO.getServerType())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.RABBIT_SERVER_TYPE_WRONG);
        }
        if (StringUtils.isBlank(paramSaveVO.getVhost())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.VIRTUAL_HOST_IS_BLANK);
        }
        if (StringUtils.isBlank(paramSaveVO.getQueueName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.QUEUE_NAME_IS_BLANK);
        }
        if (Objects.isNull(paramSaveVO.getGetNum()) || paramSaveVO.getGetNum() < 1) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GET_NUM_WRONG);
        }

        // 修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(paramSaveVO.getFixId());
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        // -----从RabbitMQ中获取消息
        RabbitGetMsgVO getMsgVO = new RabbitGetMsgVO();
        getMsgVO.setVhost(paramSaveVO.getVhost());
        getMsgVO.setQueueName(paramSaveVO.getQueueName());
        getMsgVO.setGetNum(paramSaveVO.getGetNum());

        RabbitBasisInfoVO authVO = rabbitService.getBasisInfoVO(paramSaveVO.getServerType());

        List<String> msgTextList = rabbitService.getMsgTextList(getMsgVO, authVO);
        if (CollectionUtils.isEmpty(msgTextList)) {
            return;
        }

        // -----批量保存到库中
        saveFixMsgListCommon(paramSaveVO.getFixId(), msgTextList);
    }

    /**
     * 从文本内容中批量保存修复消息
     */
    private void saveFixMsgList(MqFixMsgVO paramSaveVO) {
        // -----参数校验
        if (Objects.isNull(paramSaveVO.getFixId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }
        if (StringUtils.isBlank(paramSaveVO.getMsgTextListStr())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_LIST_EMPTY);
        }

        // 修复是否结束
        boolean fixIsEnd = fixService.selectFixIsEnd(paramSaveVO.getFixId());
        if (fixIsEnd) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        // ----- 批量保存修复消息
        List<String> msgTextList = convert2MsgTextList(paramSaveVO.getMsgTextListStr());
        saveFixMsgListCommon(paramSaveVO.getFixId(), msgTextList);
    }

    /**
     * 批量保存修复消息共用
     */
    private void saveFixMsgListCommon(Long fixId, List<String> msgTextList) {
        // 批量保存消息数据
        Integer maxOrderNum = fixMsgService.selectMaxOrderNum(fixId);
        maxOrderNum = Objects.isNull(maxOrderNum) ? 0 : maxOrderNum;

        for (String msgText : msgTextList) {
            // 保存消息文本
            MqFixMsgTextVO msgTextVO = new MqFixMsgTextVO();
            msgTextVO.setMsgText(msgText);
            Long msgTextId = fixMsgService.saveFixMsgText(msgTextVO);

            // 保存消息数据
            MqFixMsgVO msgVO = new MqFixMsgVO();

            msgVO.setFixId(fixId);
            msgVO.setMsgTextId(msgTextId);
            msgVO.setOrderNum(++maxOrderNum);
            msgVO.setFixMsgStatus(MQFixMsgStatusEnum.WAIT_FIX.getStatus());
            msgVO.setFixRemark("");
            msgVO.setDeleted(YesOrNoEnum.NO.getCode());

            fixMsgService.saveFixMsg(msgVO);
        }
    }

    /**
     * 转化字符串为消息文本列表
     */
    private List<String> convert2MsgTextList(String msgTextListStr) {
        String[] msgTextArray = msgTextListStr.split("##");

        List<String> resultList = new ArrayList<>();
        for (String msgText : msgTextArray) {
            if (StringUtils.isNotBlank(msgText)) {
                resultList.add(msgText);
            }
        }
        return resultList;
    }
}
