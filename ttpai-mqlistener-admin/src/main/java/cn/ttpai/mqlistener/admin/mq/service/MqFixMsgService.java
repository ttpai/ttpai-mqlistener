package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.mq.dao.MqFixMsgDAO;
import cn.ttpai.mqlistener.admin.mq.dao.MqFixMsgTextDAO;
import cn.ttpai.mqlistener.admin.mq.enums.MQFixMsgStatusEnum;
import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgTextVO;
import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgVO;
import cn.ttpai.mqlistener.admin.mq.model.MqFixVO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * MQ修复消息service
 *
 * @author jiayuan.su
 */
@Service
public class MqFixMsgService {

    @Autowired
    private MqFixMsgDAO fixMsgDAO;
    @Autowired
    private MqFixMsgTextDAO fixMsgTextDAO;

    /**
      * 查询修复消息分页数据
      *
      * @param queryVO 查询条件
      * @param pageInfo 分页条件
      * @return Page<MQFixMsgVO> 修复消息分页数据
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public Page<MqFixMsgVO> selectFixMsgPage(MqFixMsgVO queryVO, Pageable pageInfo) {
        if (Objects.isNull(queryVO.getFixId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }

        Long count = fixMsgDAO.selectCount(queryVO);

        if (Objects.isNull(count) || count < 1) {
            return new PageImpl<>(Collections.emptyList(), pageInfo, 0L);
        }

        List<MqFixMsgVO> fixMsgList = fixMsgDAO.selectItemList(queryVO, pageInfo);

        return new PageImpl<>(Objects.isNull(fixMsgList) ? Collections.emptyList() : fixMsgList, pageInfo, count);
    }

    /**
      * 查询最大排序数字
      *
      * @param fixId 修复ID
      * @return Integer 最大排序数字
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public Integer selectMaxOrderNum(Long fixId) {
        return fixMsgDAO.selectMaxOrderNum(fixId);
    }

    /**
      * 保存修复消息的消息文本
      *
      * @param saveVO 消息信息
      * @return Long 主键ID
      * @author jiayuan.su
      * @date 2020年09月24日
      */
    public Long saveFixMsgText(MqFixMsgTextVO saveVO) {
        return fixMsgTextDAO.saveItem(saveVO);
    }

    /**
      * 保存修复消息数据
      *
      * @param saveVO 修复消息数据
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public void saveFixMsg(MqFixMsgVO saveVO) {
        fixMsgDAO.saveItem(saveVO);
    }

    /**
      * 更新修复消息的状态
      *
      * @param fixMsgId 修复消息的ID
      * @param fixMsgStatus 修复消息的状态
      * @param fixRemark 修复消息的备注
      * @author jiayuan.su
      * @date 2020年09月14日
      */
    public void updateFixMsgStatus(Long fixMsgId, Integer fixMsgStatus, String fixRemark) {
        MqFixMsgVO updateVO = new MqFixMsgVO();
        updateVO.setId(fixMsgId);
        updateVO.setFixMsgStatus(fixMsgStatus);
        updateVO.setFixRemark(fixRemark);

        fixMsgDAO.updateItem(updateVO);
    }

    /**
      * 根据参数批量更新消息顺序
      *
      * @param fixId 修复ID
      * @param orderNum 排序数字
      * @author jiayuan.su
      * @date 2020年09月17日
      */
    public void updateOrder(Long fixId, Integer orderNum) {
        MqFixMsgVO updateVO = new MqFixMsgVO();
        updateVO.setFixId(fixId);
        updateVO.setOrderNum(orderNum);
        fixMsgDAO.updateOrder(updateVO);
    }

    /**
     * 更新消息文本
     *
     * @param msgTextId 消息文本ID
     * @param msgText 消息文本
     * @author jiayuan.su
     * @date 2020年09月17日
     */
    public void updateFixMsgText(Long msgTextId, String msgText) {
        MqFixMsgTextVO fixMsgTextVO = new MqFixMsgTextVO();
        fixMsgTextVO.setId(msgTextId);
        fixMsgTextVO.setMsgText(msgText);

        fixMsgTextDAO.updateItem(fixMsgTextVO);
    }

    /**
      * 更新修复消息的信息
      *
      * @param fixId 修复ID
      * @param orderNum 排序数字
      * @author jiayuan.su
      * @date 2020年09月17日
      */
    public void updateFixMsg(Long fixId, Integer orderNum) {
        // 更新修复消息
        MqFixMsgVO fixMsgVO = new MqFixMsgVO();
        fixMsgVO.setId(fixId);
        fixMsgVO.setOrderNum(orderNum);

        fixMsgDAO.updateItem(fixMsgVO);
    }

    /**
      * 根据修复ID和修复消息ID列表删除修复消息
      *
      * @param fixId 修复ID
      * @param fixMsgId 修复消息ID列表
      * @author jiayuan.su
      * @date 2020年09月17日
      */
    public void deleteFixMsgByIdList(Long fixId, List<Long> fixMsgId) {
        if (fixMsgId.size() == 1) {
            fixMsgDAO.deleteItemById(fixId, fixMsgId.get(0));
        } else {
            fixMsgDAO.deleteItemByIdCollection(fixId, fixMsgId);
        }
    }

    /**
      * 获取用于编辑的修复消息数据
      *
      * @param fixMsgId 修复消息ID
      * @return MQFixMsgVO 修复消息数据
      * @author jiayuan.su
      * @date 2020年09月11日
      */
    public MqFixMsgVO selectFixMsg4Edit(Long fixMsgId, boolean isWithMsgText) {
        if (Objects.isNull(fixMsgId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }

        MqFixMsgVO fixMsgVO = null;
        if (isWithMsgText) {
            fixMsgVO = fixMsgDAO.selectItemWithMsgText(fixMsgId);
        } else {
            fixMsgVO = fixMsgDAO.selectItemNoMsgText(fixMsgId);
        }

        if (Objects.isNull(fixMsgVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_DATA_NOT_EXIST);
        }
        if (!Objects.equals(fixMsgVO.getFixMsgStatus(), MQFixMsgStatusEnum.WAIT_FIX.getStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_NOT_WAIT_FIX_STATUS);
        }
        return fixMsgVO;
    }

    /**
      * 获取修复消息数据
      *
      * @param fixMsgId 修复消息ID
      * @return MQFixMsgVO 修复消息数据
      * @author jiayuan.su
      * @date 2020年09月14日
      */
    public MqFixMsgVO selectFixMsg(Long fixMsgId, boolean isWithMsgText) {
        return isWithMsgText ? fixMsgDAO.selectItemWithMsgText(fixMsgId) : fixMsgDAO.selectItemNoMsgText(fixMsgId);
    }

    /**
     * 查询修复概况
     *
     * @param fixId 修复ID
     * @return MQFixVO 修复概况
     * @author jiayuan.su
     * @date 2020年09月17日
     */
    public MqFixVO selectFixSummary(Long fixId) {
        if (Objects.isNull(fixId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }

        Integer needFixNum = fixMsgDAO.selectFixSummary(fixId, null);
        Integer waitFixNum = null;
        Integer inFixNum = null;
        Integer fixSuccessNum = null;
        Integer fixErrorNum = null;
        Integer hasNeedFix = null;
        if (needFixNum > 0) {
            waitFixNum = fixMsgDAO.selectFixSummary(fixId, MQFixMsgStatusEnum.WAIT_FIX.getStatus());
            inFixNum = fixMsgDAO.selectFixSummary(fixId, MQFixMsgStatusEnum.IN_FIX.getStatus());
            fixSuccessNum = fixMsgDAO.selectFixSummary(fixId, MQFixMsgStatusEnum.FIX_SUCCESS.getStatus());
            fixErrorNum = fixMsgDAO.selectFixSummary(fixId, MQFixMsgStatusEnum.FIX_ERROR.getStatus());
            hasNeedFix = 1;
        } else {
            waitFixNum = 0;
            inFixNum = 0;
            fixSuccessNum = 0;
            fixErrorNum = 0;
            hasNeedFix = 0;
        }


        MqFixVO fixVO = new MqFixVO();
        fixVO.setId(fixId);
        fixVO.setNeedFixNum(needFixNum);
        fixVO.setWaitFixNum(waitFixNum);
        fixVO.setInFixNum(inFixNum);
        fixVO.setFixSuccessNum(fixSuccessNum);
        fixVO.setFixErrorNum(fixErrorNum);
        fixVO.setHasNeedFix(hasNeedFix);
        fixVO.setAllFixSuccess(Objects.equals(needFixNum, fixSuccessNum) ? 1 : 0);

        return fixVO;
    }

    /**
      * 更新修复消息状态到待修复
      *
      * @param fixMsgId 修复消息ID
      * @author jiayuan.su
      * @date 2020年09月17日
      */
    public void updateFixMsgStatus2Wait(Long fixMsgId) {
        if (Objects.isNull(fixMsgId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }

        MqFixMsgVO fixMsgVO = fixMsgDAO.selectItemNoMsgText(fixMsgId);
        if (Objects.isNull(fixMsgVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }
        if (!Objects.equals(MQFixMsgStatusEnum.IN_FIX.getStatus(), fixMsgVO.getFixMsgStatus())
                && !Objects.equals(MQFixMsgStatusEnum.FIX_ERROR.getStatus(), fixMsgVO.getFixMsgStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.IN_FIX_OR_FIX_ERROR_MSG_CAN_UPDATE);
        }

        MqFixMsgVO updateVO = new MqFixMsgVO();
        updateVO.setId(fixMsgId);
        updateVO.setFixMsgStatus(MQFixMsgStatusEnum.WAIT_FIX.getStatus());
        updateVO.setFixRemark("由" + MQFixMsgStatusEnum.getEnum(fixMsgVO.getFixMsgStatus()).getText() + "重置为待修复");

        fixMsgDAO.updateItem(updateVO);
    }

    /**
     * 更新修复消息状态到修复成功
     *
     * @param fixMsgId 修复消息ID
     * @param fixRemark 修复备注
     * @author jiayuan.su
     * @date 2020年09月17日
     */
    public void updateFixMsgStatus2Success(Long fixMsgId, String fixRemark) {
        if (Objects.isNull(fixMsgId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_MSG_ID_IS_NULL);
        }
        if (StringUtils.isBlank(fixRemark)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_REMARK_IS_BLANK);
        }

        MqFixMsgVO fixMsgVO = fixMsgDAO.selectItemNoMsgText(fixMsgId);
        if (Objects.isNull(fixMsgVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }
        if (!Objects.equals(MQFixMsgStatusEnum.IN_FIX.getStatus(), fixMsgVO.getFixMsgStatus())
                && !Objects.equals(MQFixMsgStatusEnum.FIX_ERROR.getStatus(), fixMsgVO.getFixMsgStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.IN_FIX_OR_FIX_ERROR_MSG_CAN_UPDATE);
        }

        MqFixMsgVO updateVO = new MqFixMsgVO();
        updateVO.setId(fixMsgId);
        updateVO.setFixMsgStatus(MQFixMsgStatusEnum.FIX_SUCCESS.getStatus());
        updateVO.setFixRemark(fixRemark);

        fixMsgDAO.updateItem(updateVO);
    }
}
