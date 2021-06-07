package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.mq.dao.MqFixDAO;
import cn.ttpai.mqlistener.admin.mq.enums.MQFixStatusEnum;
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
 * MQ修复service
 *
 * @author jiayuan.su
 */
@Service
public class MqFixService {

    @Autowired
    private MqFixDAO fixDAO;

    /**
      * 查询修复分页数据
      *
      * @param queryVO 查询条件
      * @param pageInfo 分页条件
      * @return Page<MQFixVO> 修复分页数据
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public Page<MqFixVO> selectFixPage(MqFixVO queryVO, Pageable pageInfo) {
        // 条件处理
        if (StringUtils.isNotBlank(queryVO.getChargePerson())) {
            queryVO.setChargePersonLike(queryVO.getChargePerson() + "%");
        }
        if (Objects.nonNull(queryVO.getFixStatus()) && MQFixStatusEnum.isNotExist(queryVO.getFixStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_STATUS_ILLEGAL);
        }

        Long count = fixDAO.selectCount(queryVO);

        if (Objects.isNull(count) || count < 1) {
            return new PageImpl<>(Collections.emptyList(), pageInfo, 0L);
        }

        List<MqFixVO> fixVOList = fixDAO.selectItemList(queryVO, pageInfo);

        return new PageImpl<>(Objects.isNull(fixVOList) ? Collections.emptyList() : fixVOList, pageInfo, count);
    }

    /**
      * 根据修复ID获取修复信息
      *
      * @param id 修复ID
      * @return MQFixVO 修复信息
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public MqFixVO selectFix(Long id) {
        return fixDAO.selectItem(id);
    }

    /**
     * 查询修复是否结束
     *
     * @param fixId 修复ID
     * @return boolean true-是, false-否
     * @author jiayuan.su
     * @date 2020年09月17日
     */
    public boolean selectFixIsEnd(Long fixId) {
        if (Objects.isNull(fixId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }

        MqFixVO fixVO = fixDAO.selectItem(fixId);
        if (Objects.isNull(fixVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }

        return Objects.equals(MQFixStatusEnum.FIX_END.getStatus(), fixVO.getFixStatus());
    }

    /**
      * 获取修复数据, 以便用于编辑
      *
      * @param id 修复ID
      * @return MQFixVO 修复数据
      * @author jiayuan.su
      * @date 2020年09月11日
      */
    public MqFixVO selectFix4Edit(Long id) {
        MqFixVO fixVO = fixDAO.selectItem(id);

        if (Objects.isNull(fixVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }
        if (Objects.equals(MQFixStatusEnum.FIX_END.getStatus(), fixVO.getFixStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        return fixVO;
    }

    /**
      * 保存修复数据
      *
      * @param paramSaveVO 修复数据
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public void saveFix(MqFixVO paramSaveVO) {
        if (StringUtils.isBlank(paramSaveVO.getFixName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_NAME_IS_BLANK);
        }

        MqFixVO saveVO = new MqFixVO();
        saveVO.setFixName(paramSaveVO.getFixName());
        saveVO.setChargePerson(StringUtils.isBlank(paramSaveVO.getChargePerson()) ? "" : paramSaveVO.getChargePerson());
        saveVO.setFixStatus(MQFixStatusEnum.IN_FIX.getStatus());
        saveVO.setFixRemark("");

        fixDAO.saveItem(saveVO);
    }

    /**
      * 更新修复为结束状态
      *
      * @param fixId 修复ID
      * @param remark 备注
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public void updateFix2End(Long fixId, String remark) {
        if (Objects.isNull(fixId)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }
        if (StringUtils.isBlank(remark)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_REMARK_IS_BLANK);
        }

        MqFixVO fixVO = fixDAO.selectItem(fixId);
        if (Objects.isNull(fixVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }
        if (Objects.equals(MQFixStatusEnum.FIX_END.getStatus(), fixVO.getFixStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        MqFixVO updateVO = new MqFixVO();
        updateVO.setId(fixId);
        updateVO.setFixStatus(MQFixStatusEnum.FIX_END.getStatus());
        updateVO.setFixRemark(remark);

        fixDAO.updateItem(updateVO);
    }

    /**
      * 更新修复信息
      *
      * @param paramUpdateVO 修复信息
      * @author jiayuan.su
      * @date 2020年09月09日
      */
    public void updateFix(MqFixVO paramUpdateVO) {
        if (Objects.isNull(paramUpdateVO.getId())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_ID_IS_NULL);
        }
        if (StringUtils.isBlank(paramUpdateVO.getFixName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_NAME_IS_BLANK);
        }

        MqFixVO dbFixVO = fixDAO.selectItem(paramUpdateVO.getId());
        if (Objects.isNull(dbFixVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_NOT_EXIST);
        }
        if (Objects.equals(MQFixStatusEnum.FIX_END.getStatus(), dbFixVO.getFixStatus())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.FIX_DATA_IS_END);
        }

        MqFixVO fixVO = new MqFixVO();
        fixVO.setId(paramUpdateVO.getId());
        fixVO.setFixName(paramUpdateVO.getFixName());
        fixVO.setChargePerson(StringUtils.isBlank(paramUpdateVO.getChargePerson()) ? "" : paramUpdateVO.getChargePerson());

        fixDAO.updateItem(fixVO);
    }
}
