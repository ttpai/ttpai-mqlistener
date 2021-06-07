package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.exception.SystemException;
import cn.ttpai.mqlistener.admin.mq.dao.MqBaseConfigDAO;
import cn.ttpai.mqlistener.admin.mq.enums.MQBaseConfigTypeEnum;
import cn.ttpai.mqlistener.admin.mq.model.MqBaseConfigVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * MQ基础配置 service
 *
 * @author jiayuan.su
 */
@Service
public class MqBaseConfigService {

    @Autowired
    private MqBaseConfigDAO mqBaseConfigDAO;

    /**
     * 根据配置类型获取MQ基础配置列表
     *
     * {@link MQBaseConfigTypeEnum}
     *
     * @param configType 配置类型
     * @return List<MQBaseConfigVO> MQ基础配置列表
     * @author jiayuan.su
     * @date 2020年04月16日
     */
    public List<MqBaseConfigVO> selectMqBaseConfigList(Integer configType) {
        if (Objects.isNull(configType)) {
            return Collections.emptyList();
        }
        return mqBaseConfigDAO.selectMqBaseConfig(configType);
    }

    /**
     * 更新MQ基础配置
     *
     * @param mqBaseConfigVOList MQ基础配置列表
     * @author jiayuan.su
     * @date 2020年04月16日
     */
    public void updateMqBaseConfigWithList(List<MqBaseConfigVO> mqBaseConfigVOList) {
        if (CollectionUtils.isEmpty(mqBaseConfigVOList)) {
            return;
        }

        for (MqBaseConfigVO mqBaseConfigVO : mqBaseConfigVOList) {
            mqBaseConfigDAO.updateMqBaseConfig(mqBaseConfigVO);
        }
    }

    /**
     * 填充配置
     *
     * @param mqBaseConfigVOList MQ基础配置列表
     * @param obj 填充的类
     * @author jiayuan.su
     * @date 2020年04月16日
     */
    public <T> void fillConfig(List<MqBaseConfigVO> mqBaseConfigVOList, T obj) {
        try {
            if (CollectionUtils.isEmpty(mqBaseConfigVOList)) {
                return;
            }

            for (MqBaseConfigVO mqBaseConfigVO : mqBaseConfigVOList) {
                Field field = obj.getClass().getDeclaredField(mqBaseConfigVO.getParamName());

                if (Objects.isNull(field)) {
                    continue;
                }

                // 字段类型
                Class<?> fieldClazz = field.getType();

                field.setAccessible(true);
                if (Integer.class == fieldClazz) {
                    field.set(obj, Integer.valueOf(mqBaseConfigVO.getParamValue()));
                } else {
                    throw new UnsupportedOperationException("类型不支持, 请扩展");
                }
            }
        } catch (Exception e) {
            throw new SystemException("MQ基础配置错误");
        }
    }
}
