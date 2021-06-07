package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.PagelabVO;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;
import cn.ttpai.mqlistener.admin.mq.dao.MqServiceInfoDAO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoConditionVO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoVO;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * MQ服务信息service
 *
 * @author jiayuan.su
 */
@Service
public class MqServiceInfoService {

    @Autowired
    private MqServiceInfoDAO serviceInfoDAO;

    /**
      * 通过主键ID查询MQ服务信息
      *
      * @param serviceInfoId 服务信息ID
      * @return: MQServiceInfoVO MQ服务信息
      * @author jiayuan.su
      * @date 2020年07月24日
      */
    public MqServiceInfoVO selectMqServiceInfoById(Long serviceInfoId) {
        if (Objects.isNull(serviceInfoId)) {
            return null;
        }
        return serviceInfoDAO.selectById(serviceInfoId);
    }

    /**
      * 查询MQ服务信息分页数据
      *
      * @param conditionVO 查询条件
      * @param pageInfo 分页条件
      * @return PagelabVO<MQServiceInfoVO> MQ服务信息分页数据
      * @author jiayuan.su
      * @date 2020年07月24日
      */
    public PagelabVO<MqServiceInfoVO> selectMqServiceInfoPage(MqServiceInfoConditionVO conditionVO, Pageable pageInfo) {
        if (Objects.isNull(conditionVO)) {
            conditionVO = new MqServiceInfoConditionVO();
        }
        if (StringUtil.isNotBlank(conditionVO.getServiceName())) {
            conditionVO.setServiceNameLike("%" + conditionVO.getServiceName() + "%");
        }
        conditionVO.setDeleted(0);
        checkPageInfo(pageInfo);

        Integer count = serviceInfoDAO.selectCountByCondition(conditionVO);

        PagelabVO<MqServiceInfoVO> resultPage = new PagelabVO<>(pageInfo.getPageNumber() + 1, pageInfo.getPageSize());
        if (!isHasRecord(count)) {
            resultPage.setTotalNum(0);
            resultPage.setDataList(Collections.emptyList());
            return resultPage;
        }

        List<MqServiceInfoVO> serviceInfoVOList = serviceInfoDAO.selectListByCondition(conditionVO, pageInfo);
        resultPage.setTotalNum(count);
        resultPage.setDataList(serviceInfoVOList);
        return resultPage;
    }

    /**
      * 保存MQ服务信息
      *
      * @param serviceInfoVO 服务信息
      * @author jiayuan.su
      * @date 2020年07月24日
      */
    public void saveMqServiceInfo(MqServiceInfoVO serviceInfoVO) {
        boolean serviceNameIsExist = selectServiceNameIsExist(serviceInfoVO.getServiceName(), null);
        if (serviceNameIsExist) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SERVICE_ALREADY_HAVE);
        }

        MqServiceInfoVO dbServiceInfoVO = serviceInfoDAO.selectByServiceName(serviceInfoVO.getServiceName());

        if (Objects.isNull(dbServiceInfoVO)) {
            serviceInfoDAO.saveByVO(serviceInfoVO);
        } else {
            serviceInfoDAO.updateByServiceName(serviceInfoVO);
        }
    }

    /**
      * 保存服务列表
      *
      * @param serviceInfoVOList 服务列表
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public void saveMqServiceInfoList(List<MqServiceInfoVO> serviceInfoVOList) {
        if (CollectionUtils.isEmpty(serviceInfoVOList)) {
            return;
        }

        serviceInfoVOList.get(serviceInfoVOList.size() - 1).setComma("");
        serviceInfoDAO.saveMqServiceInfoList(serviceInfoVOList);
    }

    /**
      * 更新组信息
      *
      * @param conditionVO 条件
      * @return boolean true-更新了记录, false-未更新记录
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public boolean updateGroup(MqServiceInfoConditionVO conditionVO) {
        Integer value = serviceInfoDAO.updateGroup(conditionVO);
        return !Objects.isNull(value) && value > 0;
    }

    /**
      * 更新服务信息
      *
      * @param serviceInfoVO 服务信息
      * @return boolean true-更新了记录, false-未更新记录
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public boolean updateMqServiceInfo(MqServiceInfoVO serviceInfoVO) {
        boolean serviceNameIsExist = selectServiceNameIsExist(serviceInfoVO.getServiceName(), serviceInfoVO.getId());
        if (serviceNameIsExist) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SERVICE_ALREADY_HAVE);
        }

        Integer value = serviceInfoDAO.updateById(serviceInfoVO);
        return !Objects.isNull(value) && value > 0;
    }

    /**
      * 删除服务
      *
      * @param serviceId 服务ID
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public void deleteServiceInfo(Long serviceId) {
        serviceInfoDAO.deleteServiceInfo(serviceId);
    }

    /**
     * 检查分页信息
     */
    private void checkPageInfo(Pageable pageInfo) {
        if (Objects.isNull(pageInfo)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PAGE_INFO_NULL);
        }
        if (pageInfo.getPageNumber() < 0) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.NOW_PAGE_ILLEGAL);
        }
        if (pageInfo.getPageSize() < 0 || pageInfo.getPageSize() > 20) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PAGE_SIZE_ILLEGAL);
        }
    }

    /**
     * 查询服务名是否存在
     */
    private boolean selectServiceNameIsExist(String serviceName, Long serviceId) {
        Integer count = serviceInfoDAO.selectCountByServiceName(serviceName, serviceId);
        return !Objects.isNull(count) && count > 0;
    }

    /**
     * 判断是否有记录
     */
    private boolean isHasRecord(Integer count) {
        return !Objects.isNull(count) && count > 0;
    }
}
