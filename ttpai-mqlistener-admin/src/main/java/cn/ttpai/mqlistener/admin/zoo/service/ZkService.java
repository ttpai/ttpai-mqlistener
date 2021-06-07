package cn.ttpai.mqlistener.admin.zoo.service;

import cn.ttpai.mqlistener.admin.common.component.zk.ZooManager;
import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.common.util.StrFillUtil;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;
import cn.ttpai.mqlistener.admin.zoo.constant.Constants;
import cn.ttpai.mqlistener.admin.zoo.enums.ListenerStatusEnum;
import cn.ttpai.mqlistener.admin.zoo.enums.ProcessTypeEnum;
import cn.ttpai.mqlistener.admin.zoo.model.ListenerConfig;
import cn.ttpai.mqlistener.admin.zoo.model.ListenerInfo;
import cn.ttpai.mqlistener.admin.zoo.model.NodeVO;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * zookeeper节点service
 *
 * @author jiayuan.su
 */
@Service
public class ZkService {
    private static final Logger logger = LoggerFactory.getLogger(ZkService.class);

    @Autowired
    private ZooManager zooManager;

    /**
     * 获取项目列表
     *
     * @return List<String> 项目列表
     * @author jiayuan.su
     * @date 2019年04月09日
     */
    public List<String> selectProjectList() {
        if (!zooManager.isZooRootPathExist()) {
            return Collections.emptyList();
        }

        List<String> projectList = zooManager.getRootChildrenFromZoo();
        if (CollectionUtils.isEmpty(projectList)) {
            return Collections.emptyList();
        }

        Collections.sort(projectList);
        return projectList;
    }

    /**
     * 获取项目下listener列表
     *
     * @param projectName 项目名称
     * @return List<String> 项目下的listener列表
     * @author jiayuan.su
     * @date 2019年04月09日
     */
    public List<String> selectListenerList(String projectName) {
        if (StringUtil.isBlank(projectName)) {
            logger.error("项目名参数缺失[projectName:{}]", projectName);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PROJECT_NAME_BLANK);
        }

        String projectPath = StrFillUtil.item("projectName", projectName).target(Constants.ZooNode.NODE_PATH_PROJECT);
        if (!zooManager.isZooPathExist(projectPath)) {
            logger.error("项目名对应数据找不到[projectName:{}, projectPath:{}]", projectName, projectPath);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PROJECT_DATA_NOT_FOUND);
        }

        List<String> listenerList = zooManager.getChildrenFromZoo(projectPath);
        if (CollectionUtils.isEmpty(listenerList)) {
            return Collections.emptyList();
        }

        Collections.sort(listenerList);
        return listenerList;
    }

    /**
     * 删除项目
     *
     * @param projectName 项目名称
     * @author jiayuan.su
     * @date 2019年04月19日
     */
    public void deleteProject(String projectName) {
        if (StringUtil.isBlank(projectName)) {
            logger.error("项目名参数缺失[projectName:{}]", projectName);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PROJECT_NAME_BLANK);
        }

        String projectPath = StrFillUtil.item("projectName", projectName).target(Constants.ZooNode.NODE_PATH_PROJECT);
        if (zooManager.isZooPathExist(projectPath)) {
            zooManager.deleteZooPath(projectPath);
        }
    }

    /**
     * 删除某监听器
     *
     * @param projectName 项目名
     * @param listenerName 监听器名
     * @author jiayuan.su
     * @date 2019年04月15日
     */
    public void deleteListener(String projectName, String listenerName) {
        if (StringUtil.isBlank(projectName)) {
            logger.error("项目名参数缺失[projectName:{}]", projectName);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.PROJECT_NAME_BLANK);
        }
        if (StringUtil.isBlank(listenerName)) {
            logger.error("监听器参数缺失[listenerName:{}]", listenerName);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_NAME_BLANK);
        }

        String listenerPath = StrFillUtil.item("projectName", projectName).item("listenerName", listenerName)
                .target(Constants.ZooNode.NODE_PATH_PROJECT_LISTENER);
        if (zooManager.isZooPathExist(listenerPath)) {
            zooManager.deleteZooPath(listenerPath);
        }
    }

    /**
     * 获取监听器详细信息
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @return ListenerInfo 监听器详细信息
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public ListenerInfo selectListenerInfo(String listenerStr) {
        String[] listenerSplit = resolveListenerStr(listenerStr);
        final String projectName = listenerSplit[0];
        final String listenerName = listenerSplit[1];

        String listenerPath = getListenerPath(projectName, listenerName);
        String configPath = getListenerConfigPath(listenerPath);
        String descPath = getListenerDescPath(listenerPath);
        String managerPath = getListenerManagerPath(listenerPath);

        String queue = (zooManager.isZooPathExist(listenerPath) ? zooManager.getDataFromZoo(listenerPath) : null);
        String config = (zooManager.isZooPathExist(configPath) ? zooManager.getDataFromZoo(configPath) : null);
        String desc = (zooManager.isZooPathExist(descPath) ? zooManager.getDataFromZoo(descPath) : null);
        String manager = (zooManager.isZooPathExist(managerPath) ? zooManager.getDataFromZoo(managerPath) : null);

        ListenerInfo listenerInfo = new ListenerInfo();
        listenerInfo.setName(listenerName);
        listenerInfo.setZooPath(listenerPath);
        listenerInfo.setQueue(queue);
        listenerInfo.setDesc(desc);
        listenerInfo.setManager(manager);
        listenerInfo.setListenerConfig(buildListenerConfig(config));

        return listenerInfo;
    }

    /**
     * 保存监听器配置信息
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @param listenerConfig 监听器配置
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public void saveListenerConfig(String listenerStr, ListenerConfig listenerConfig) {
        if (null == listenerConfig) {
            logger.error("监听器配置参数缺失[listenerConfig:{}]", "null");
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_CONFIG_NULL);
        }

        String configPath = getListenerConfigPath(getListenerPath(listenerStr));
        if (!zooManager.isZooPathExist(configPath)) {
            logger.error("监听器对应配置数据不存在[listenerStr:{}, configPath:{}]", listenerStr, configPath);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_CONFIG_MISSING);
        }

        zooManager.setData2Zoo(configPath, JsonUtil.toJsonString(listenerConfig, false));
    }

    /**
     * 保存监听器描述信息
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @param data 描述信息
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public void saveListenerDesc(String listenerStr, String data) {
        if (StringUtil.isBlank(data)) {
            logger.error("监听器描述信息缺失[data:{}]", data);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_DESC_BLANK);
        }

        String descPath = getListenerDescPath(getListenerPath(listenerStr));
        if (!zooManager.isZooPathExist(descPath)) {
            logger.error("监听器对应描述数据不存在[listenerStr:{}, descPath:{}]", listenerStr, descPath);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_DESC_MISSING);
        }

        zooManager.setData2Zoo(descPath, data);
    }

    /**
     * 保存监听器管理员信息
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @param manager 管理员
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public void saveListenerManager(String listenerStr, String manager) {
        if (StringUtil.isBlank(manager)) {
            logger.error("监听器管理员信息缺失[data:{}]", manager);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_MANAGER_BLANK);
        }

        String managerPath = getListenerManagerPath(getListenerPath(listenerStr));
        if (!zooManager.isZooPathExist(managerPath)) {
            logger.error("监听器对应管理员数据不存在[listenerStr:{}, managerPath:{}]", listenerStr, managerPath);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_MANAGER_MISSING);
        }

        zooManager.setData2Zoo(managerPath, manager);
    }

    /**
     * 获取某监听器的实例列表信息
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @return List<NodeVO> 监听器实例列表信息
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public List<NodeVO> selectListenerInstanceList(String listenerStr) {
        String instancesPath = getListenerInstancesPath(getListenerPath(listenerStr));
        if (!zooManager.isZooPathExist(instancesPath)) {
            return Collections.emptyList();
        }

        List<String> instanceList = zooManager.getChildrenFromZoo(instancesPath);

        if (CollectionUtils.isEmpty(instanceList)) {
            return Collections.emptyList();
        }

        Collections.sort(instanceList);
        List<NodeVO> nodeVOList = new ArrayList<>();
        for (String instance : instanceList) {
            NodeVO nodeVO = new NodeVO();
            nodeVO.setPath(instance);

            String instancePath = getInstancePath(instancesPath, instance);

            Integer inUse = resolveListenerInstanceInUse(zooManager.getDataFromZoo(instancePath));
            nodeVO.setInUse(inUse);

            Integer online = zooManager.isZooPathExist(getInstanceOnlinePath(instancePath)) ? 1 : 0;
            nodeVO.setOnline(online);

            String errorDescPath = getInstanceErrorDescPath(instancePath);
            if (zooManager.isZooPathExist(errorDescPath)) {
                String errorDesc = zooManager.getDataFromZoo(errorDescPath);
                if (StringUtil.isNotBlank(errorDesc)) {
                    String[] info = errorDesc.split("\\$-\\$");
                    if (info.length == 3){
                        nodeVO.setErrorInfo(info[0]);
                        nodeVO.setErrorTime(info[1]);
                        nodeVO.setErrorMsg(info[2]);
                    }
                }
            }

            nodeVOList.add(nodeVO);
        }

        return nodeVOList;
    }

    /**
     * 更新监听器实例的状态
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @param instance 监听器下的实例
     * @param use 使用否 {@link ListenerStatusEnum}
     * @author jiayuan.su
     * @date 2019年04月10日
     */
    public void updateListenerInstanceStatus(String listenerStr, String instance, String use) {
        if (StringUtil.isBlank(instance) || !ListenerStatusEnum.isExist(use)) {
            logger.error("实例名称/实例启用数据缺失[instance:{},use:{}]", instance, use);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.INSTANCE_NAME_OR_DATA_MISSING);
        }

        String listenerPath = getListenerPath(listenerStr);
        String instancesPath = getListenerInstancesPath(listenerPath);
        String insancePath = getInstancePath(instancesPath, instance);

        if (!zooManager.isZooPathExist(insancePath)) {
            logger.error("路径不存在[insancePath:{}]", insancePath);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.INSTANCE_DATA_MISSING);
        }

        if (ListenerStatusEnum.START.getCode().equals(use)) {
            String configPath = getListenerConfigPath(listenerPath);
            if (!isListenerConfigValueNotBlank(configPath)) {
                logger.error("[{}]对应的监听器配置为空,不进行启动", listenerStr);
                throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_CONFIG_BLANK_CANNOT_START);
            }
            String descPath = getListenerDescPath(listenerPath);
            if (!isListenerConfigValueNotBlank(descPath)) {
                logger.error("[{}]对应的监听器描述为空,不进行启动", listenerStr);
                throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_DESC_BLANK_CANNOT_START);
            }
            String managerPath = getListenerManagerPath(listenerPath);
            if (!isListenerConfigValueNotBlank(managerPath)) {
                logger.error("[{}]对应的监听器管理员为空,不进行启动", listenerStr);
                throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_MANAGER_BLANK_CANNOT_START);
            }
        }

        zooManager.setData2Zoo(insancePath, String.valueOf(use));
    }

    /**
     * 删除监听器实例
     *
     * @param listenerStr 监听器字符串 示例：projectName/listenerName
     * @param instance 监听器下的实例
     * @author jiayuan.su
     * @date 2019年04月20日
     */
    public void deleteInstance(String listenerStr, String instance) {
        if (StringUtil.isBlank(instance)) {
            logger.error("实例名称缺失[instance:{}]", instance);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.INSTANCE_DATA_BLANK);
        }

        String listenerPath = getListenerPath(listenerStr);
        String instancesPath = getListenerInstancesPath(listenerPath);
        String insancePath = getInstancePath(instancesPath, instance);

        if (zooManager.isZooPathExist(insancePath)) {
            zooManager.deleteZooPath(insancePath);
        }
    }

    /**
     * 监听器配置值是否存在
     */
    private boolean isListenerConfigValueNotBlank(String configPath) {
        if (!this.zooManager.isZooPathExist(configPath)) {
            return false;
        }

        String configStr = this.zooManager.getDataFromZoo(configPath);
        if (StringUtil.isBlank(configStr)) {
            return false;
        }
        return true;
    }

    /**
     * 解析listener path (projectName/listenerName)
     */
    private static String[] resolveListenerStr(String listenerStr) {
        if (StringUtil.isBlank(listenerStr)) {
            logger.error("监听器路径非法[{}]", listenerStr);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_PATH_ILLEGAL);
        }
        String[] listenerSplit = listenerStr.split(Constants.FORWARD_SLASH);
        if (listenerSplit.length != 2) {
            logger.error("监听器路径非法[{}]", listenerStr);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_PATH_ILLEGAL);
        }
        if (StringUtil.isBlank(listenerSplit[0]) || StringUtil.isBlank(listenerSplit[1])) {
            logger.error("监听器路径非法[{}]", listenerStr);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.LISTENER_PATH_ILLEGAL);
        }

        return listenerSplit;
    }

    /**
     * 获取listener路径
     */
    private String getListenerPath(String listenerStr) {
        String[] listenerSplit = resolveListenerStr(listenerStr);
        final String projectName = listenerSplit[0];
        final String listenerName = listenerSplit[1];

        return getListenerPath(projectName, listenerName);
    }

    /**
     * 获取listener路径
     */
    private String getListenerPath(String projectName, String listenerName) {
        return StrFillUtil.item("projectName", projectName).item("listenerName", listenerName).target(Constants.ZooNode.NODE_PATH_PROJECT_LISTENER);
    }

    /**
     * 获取listener配置路径
     */
    private String getListenerConfigPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(Constants.ZooNode.NODE_PATH_LISTENER_CONFIG);
    }

    /**
     * 获取listener描述路径
     */
    private String getListenerDescPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(Constants.ZooNode.NODE_PATH_LISTENER_DESC);
    }

    /**
     * 获取listener管理员路径
     */
    private String getListenerManagerPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(Constants.ZooNode.NODE_PATH_LISTENER_MANAGER);
    }

    /**
     * 获取listerner所有实例路径
     */
    private String getListenerInstancesPath(String listenerPath) {
        return StrFillUtil.item("listenerPath", listenerPath).target(Constants.ZooNode.NODE_PATH_LISTENER_INSTANCES);
    }

    /**
     * 获取listener具体实例路径
     */
    private String getInstancePath(String instancesPath, String instance) {
        return StrFillUtil.item("instancesPath", instancesPath).item("instance", instance).target(Constants.ZooNode.NODE_PATH_LISTENER_INSTANCE);
    }

    /**
     * 获取实例是否在线路径
     */
    private String getInstanceOnlinePath(String instancePath) {
        return StrFillUtil.item("instancePath", instancePath).target(Constants.ZooNode.NODE_PATH_INSTANCE_ONLINE);
    }

    /**
     * 获取实例错误描述路径
     */
    private String getInstanceErrorDescPath(String instancePath) {
        return StrFillUtil.item("instancePath", instancePath).target(Constants.ZooNode.NODE_PATH_INSTANCE_ERROR_DESC);
    }

    /**
     * listener实例是否在运行
     */
    private Integer resolveListenerInstanceInUse(String paramInUse) {
        if (!Constants.STR_ZERO.equals(paramInUse) && !Constants.STR_ONE.equals(paramInUse)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.INSTANCE_IN_USE_MISSING);
        }
        return Integer.parseInt(paramInUse);
    }

    /**
     * 构建ListenerConfig
     */
    private ListenerConfig buildListenerConfig(String listenerConfigStr) {
        if (StringUtil.isBlank(listenerConfigStr)) {
            return null;
        }

        ListenerConfig listenerConfig = JsonUtil.parseObject(listenerConfigStr, ListenerConfig.class);
        if (null == listenerConfig) {
            return null;
        }

        if (null == listenerConfig.getProcessType()) {
            doListenerConfigForOldVersion(listenerConfig);
        }

        return listenerConfig;
    }

    /**
     * 老版本
     */
    private void doListenerConfigForOldVersion(ListenerConfig listenerConfig) {
        if (StringUtil.isNotBlank(listenerConfig.getExceptionQueue())) {
            listenerConfig.setProcessType(ProcessTypeEnum.EXCEPTION_QUEUE.getCode());
        } else {
            listenerConfig.setProcessType(ProcessTypeEnum.REPUT_QUEUE.getCode());
        }
    }
}
