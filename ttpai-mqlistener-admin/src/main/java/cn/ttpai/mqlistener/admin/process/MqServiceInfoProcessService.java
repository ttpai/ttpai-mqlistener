package cn.ttpai.mqlistener.admin.process;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.enums.YesOrNoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceGroupVO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoConditionVO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoVO;
import cn.ttpai.mqlistener.admin.mq.service.MqServiceGroupService;
import cn.ttpai.mqlistener.admin.mq.service.MqServiceInfoService;
import cn.ttpai.mqlistener.admin.mq.service.MqSyncDataService;
import cn.ttpai.mqlistener.admin.zoo.service.ZkService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MQ服务信息process service
 *
 * @author jiayuan.su
 */
@Service
public class MqServiceInfoProcessService {

    @Autowired
    private MqSyncDataService syncDataService;
    @Autowired
    private ZkService zkService;
    @Autowired
    private MqServiceInfoService serviceInfoService;
    @Autowired
    private MqServiceGroupService serviceGroupService;

    /**
      * 从zk中更新服务到数据库中
      *
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public void updateServiceFromZk2Db() {
        // 未开启，直接返回
        if (!syncDataService.selectSyncOpenIsOpen()) {
            return;
        }

        // 项目列表
        List<String> projectList = zkService.selectProjectList();

        // 开始处理
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        List<MqServiceInfoVO> serviceInfoVOList = new ArrayList<>();
        for (String project : projectList) {
            MqServiceInfoVO serviceInfoVO = new MqServiceInfoVO();

            serviceInfoVO.setServiceName(project);
            serviceInfoVO.setServiceDesc("");
            serviceInfoVO.setGroupId(-1L);
            serviceInfoVO.setGroupName("");
            serviceInfoVO.setDeleted(YesOrNoEnum.NO.getCode());

            serviceInfoVOList.add(serviceInfoVO);
        }

        serviceInfoService.saveMqServiceInfoList(serviceInfoVOList);
        syncDataService.updateSyncOpen2Close();
    }

    /**
      * 删除服务
      *
      * @param serviceId 服务ID
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public void deleteService(Long serviceId) {
        MqServiceInfoVO serviceInfoVO = serviceInfoService.selectMqServiceInfoById(serviceId);
        if (Objects.isNull(serviceInfoVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SERVICE_NOT_EXIST);
        }

        // 删除库中记录
        serviceInfoService.deleteServiceInfo(serviceId);

        // 删除zk中记录
        zkService.deleteProject(serviceInfoVO.getServiceName());
    }

    /**
      * 更新服务信息
      *
      * @param serviceInfoVO 服务信息
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public void updateService(MqServiceInfoVO serviceInfoVO) {
        checkServiceInfoAndFillDefault(serviceInfoVO);

        // 组名
        MqServiceGroupVO serviceGroupVO = serviceGroupService.selectServiceGroup(serviceInfoVO.getGroupId());
        if (Objects.isNull(serviceGroupVO)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_NOT_EXIST);
        }
        serviceInfoVO.setGroupName(serviceGroupVO.getGroupName());

        if (Objects.isNull(serviceInfoVO.getId())) {
            // 新增
            serviceInfoService.saveMqServiceInfo(serviceInfoVO);
        } else {
            // 编辑
            serviceInfoService.updateMqServiceInfo(serviceInfoVO);
        }
    }

    /**
      * 更新组信息
      *
      * @param serviceGroupVO 组信息
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public void updateServiceGroup(MqServiceGroupVO serviceGroupVO) {
        // 更新组信息
        serviceGroupService.updateServiceGroup(serviceGroupVO);

        // 关联服务更新组信息
        MqServiceInfoConditionVO conditionVO = new MqServiceInfoConditionVO();
        conditionVO.setGroupId(serviceGroupVO.getId());
        conditionVO.setGroupName(serviceGroupVO.getGroupName());
        conditionVO.setClearGroup(YesOrNoEnum.NO.getCode());
        serviceInfoService.updateGroup(conditionVO);
    }

    /**
      * 删除组信息
      *
      * @param groupId 组ID
      * @author jiayuan.su
      * @date 2020年07月28日
      */
    public void deleteGroup(Long groupId) {
        MqServiceGroupVO serviceGroupVO = serviceGroupService.selectServiceGroup(groupId);
        if (Objects.isNull(serviceGroupVO)) {
            return;
        }

        // 删除组
        serviceGroupService.deleteGroup(groupId);
        // 关联服务删除组信息
        MqServiceInfoConditionVO conditionVO = new MqServiceInfoConditionVO();
        conditionVO.setGroupId(groupId);
        conditionVO.setClearGroup(YesOrNoEnum.YES.getCode());
        serviceInfoService.updateGroup(conditionVO);
    }

    /**
     * 校验服务信息并填充默认值
     */
    private void checkServiceInfoAndFillDefault(MqServiceInfoVO serviceInfoVO) {
        if (StringUtil.isBlank(serviceInfoVO.getServiceName())) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SERVICE_NAME_BLANK);
        }
        if (Objects.isNull(serviceInfoVO.getGroupId()) || serviceInfoVO.getGroupId() < 0) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.GROUP_ID_IS_NULL);
        }
        if (StringUtil.isBlank(serviceInfoVO.getServiceDesc())) {
            serviceInfoVO.setServiceDesc("");
        }
        serviceInfoVO.setDeleted(0);
    }
}
