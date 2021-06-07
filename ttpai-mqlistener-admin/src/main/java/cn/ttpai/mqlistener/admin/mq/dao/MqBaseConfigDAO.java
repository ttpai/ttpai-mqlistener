package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqBaseConfigVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

/**
 * MQ基础配置 DAO
 *
 * @author jiayuan.su
 */
@DAO
public interface MqBaseConfigDAO {

    @SQL("SELECT ID, CONFIG_TYPE, PARAM_NAME, PARAM_VALUE, CREATE_TIME, MODIFY_TIME" +
            " FROM MQ_BASE_CONFIG" +
            " WHERE CONFIG_TYPE = :configType")
    List<MqBaseConfigVO> selectMqBaseConfig(@SQLParam("configType") Integer configType);

    @SQL("UPDATE MQ_BASE_CONFIG" +
            " SET PARAM_VALUE = :vo.paramValue" +
            " WHERE CONFIG_TYPE = :vo.configType" +
            " AND PARAM_NAME = :vo.paramName")
    void updateMqBaseConfig(@SQLParam("vo") MqBaseConfigVO vo);
}
