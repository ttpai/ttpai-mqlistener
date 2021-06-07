package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.mq.dao.MqServiceGroupDAO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceGroupVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * MQ服务组service
 *
 * @author jiayuan.su
 */
@Service
public class MqServiceGroupService {

    @Autowired
    private MqServiceGroupDAO serviceGroupDAO;

    /**
      * 查询有效的服务组列表
      *
      * @return List<MQServiceGroupVO> 有效的服务组列表
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public List<MqServiceGroupVO> selectMqServiceGroupList() {
        List<MqServiceGroupVO> serviceGroupVOList = serviceGroupDAO.selectMqServiceGroupList();

        return Objects.isNull(serviceGroupVOList) ? Collections.emptyList() : serviceGroupVOList;
    }

    /**
      * 根据组ID查找组信息
      *
      * @param groupId 组ID
      * @return MQServiceGroupVO 组信息
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public MqServiceGroupVO selectServiceGroup(Long groupId) {
        if (Objects.isNull(groupId)) {
            return null;
        }
        return serviceGroupDAO.selectServiceGroup(groupId);
    }

    /**
      * 保存组信息
      *
      * @param serviceGroupVO 组信息
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public void saveServiceGroup(MqServiceGroupVO serviceGroupVO) {
        boolean groupNameIsExist = selectGroupNameIsExist(serviceGroupVO.getGroupName(), null);
        if (groupNameIsExist) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_ALREADY_HAVE);
        }

        MqServiceGroupVO dbServiceGroupVO = serviceGroupDAO.selectByGroupName(serviceGroupVO.getGroupName());

        if (Objects.isNull(dbServiceGroupVO)) {
            serviceGroupDAO.saveMqServiceGroup(serviceGroupVO);
        } else {
            serviceGroupDAO.updateByGroupName(serviceGroupVO);
        }
    }

    /**
      * 更新组信息
      *
      * @param serviceGroupVO 组信息
      * @return boolean true-更新了记录, false-未更新记录
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public boolean updateServiceGroup(MqServiceGroupVO serviceGroupVO) {
        boolean groupNameIsExist = selectGroupNameIsExist(serviceGroupVO.getGroupName(), serviceGroupVO.getId());
        if (groupNameIsExist) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_ALREADY_HAVE);
        }

        Integer value = serviceGroupDAO.updateById(serviceGroupVO);
        return !Objects.isNull(value) && value > 0;
    }

    /**
     * 查询组名是否存在
     */
    private boolean selectGroupNameIsExist(String groupName, Long groupId) {
        Integer count = serviceGroupDAO.selectCountByGroupName(groupName, groupId);
        return !Objects.isNull(count) && count > 0;
    }

    /**
      * 根据组ID删除组信息
      *
      * @param groupId 组信息
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public void deleteGroup(Long groupId) {
        serviceGroupDAO.deleteGroup(groupId);
    }
}
