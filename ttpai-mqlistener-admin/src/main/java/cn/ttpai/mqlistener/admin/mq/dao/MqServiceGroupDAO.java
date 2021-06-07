package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqServiceGroupVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

/**
 * MQ服务组DAO
 * <p>
 * @author jiayuan.su
 */
@DAO
public interface MqServiceGroupDAO {

    String ALL_FIELD = "ID, GROUP_NAME, GROUP_DESC, DELETED, CREATE_TIME, MODIFY_TIME";

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_GROUP" +
            " WHERE DELETED = 0")
    List<MqServiceGroupVO> selectMqServiceGroupList();

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_SERVICE_GROUP" +
            " WHERE GROUP_NAME = :groupName" +
            " AND DELETED = 0" +
            " #if(:groupId != null){AND ID != :groupId}")
    Integer selectCountByGroupName(@SQLParam("groupName") String groupName,
                                   @SQLParam("groupId") Long groupId);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_GROUP" +
            " WHERE GROUP_NAME = :groupName" +
            " AND DELETED = 1")
    MqServiceGroupVO selectByGroupName(@SQLParam("groupName") String groupName);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_GROUP" +
            " WHERE ID = :groupId" +
            " AND DELETED = 0")
    MqServiceGroupVO selectServiceGroup(@SQLParam("groupId") Long groupId);

    @SQL("INSERT INTO MQ_SERVICE_GROUP" +
            " (" + ALL_FIELD + ")" +
            " VALUES" +
            " (NULL, :vo.groupName, :vo.groupDesc, :vo.deleted, NOW(), NOW())")
    void saveMqServiceGroup(@SQLParam("vo") MqServiceGroupVO vo);

    @SQL("UPDATE MQ_SERVICE_GROUP" +
            " SET" +
            " GROUP_NAME = :vo.groupName," +
            " GROUP_DESC = :vo.groupDesc," +
            " DELETED = :vo.deleted" +
            " WHERE ID = :vo.id")
    Integer updateById(@SQLParam("vo") MqServiceGroupVO vo);

    @SQL("UPDATE MQ_SERVICE_GROUP" +
            " SET" +
            " GROUP_DESC = :vo.groupDesc," +
            " DELETED = :vo.deleted" +
            " WHERE GROUP_NAME = :vo.groupName")
    Integer updateByGroupName(@SQLParam("vo") MqServiceGroupVO vo);

    @SQL("UPDATE MQ_SERVICE_GROUP" +
            " SET" +
            " DELETED = 1" +
            " WHERE ID = :groupId")
    void deleteGroup(@SQLParam("groupId") Long groupId);
}
