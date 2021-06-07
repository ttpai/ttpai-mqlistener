package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoConditionVO;
import cn.ttpai.mqlistener.admin.mq.model.MqServiceInfoVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * MQ服务信息DAO
 *
 * @author jiayuan.su
 */
@DAO
public interface MqServiceInfoDAO {

    String ALL_FIELD = "ID, SERVICE_NAME, SERVICE_DESC, GROUP_ID, GROUP_NAME, DELETED, CREATE_TIME, MODIFY_TIME";

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_INFO" +
            " WHERE ID = :id" +
            " AND DELETED = 0")
    MqServiceInfoVO selectById(@SQLParam("id") Long id);

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_SERVICE_INFO" +
            " WHERE SERVICE_NAME = :serviceName" +
            " AND DELETED = 0" +
            " #if(:serviceId != null){AND ID != :serviceId}")
    Integer selectCountByServiceName(@SQLParam("serviceName") String serviceName,
                                     @SQLParam("serviceId") Long serviceId);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_INFO" +
            " WHERE SERVICE_NAME = :serviceName" +
            " AND DELETED = 1")
    MqServiceInfoVO selectByServiceName(@SQLParam("serviceName") String serviceName);

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_SERVICE_INFO" +
            " WHERE 1 = 1" +
            " #if(:conditionVO.serviceNameLike != null){AND SERVICE_NAME LIKE :conditionVO.serviceNameLike}" +
            " #if(:conditionVO.groupId != null){AND GROUP_ID = :conditionVO.groupId}" +
            " #if(:conditionVO.deleted != null){AND DELETED = :conditionVO.deleted}")
    Integer selectCountByCondition(@SQLParam("conditionVO") MqServiceInfoConditionVO conditionVO);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_SERVICE_INFO" +
            " WHERE 1 = 1" +
            " #if(:conditionVO.serviceNameLike != null){AND SERVICE_NAME LIKE :conditionVO.serviceNameLike}" +
            " #if(:conditionVO.groupId != null){AND GROUP_ID = :conditionVO.groupId}" +
            " #if(:conditionVO.deleted != null){AND DELETED = :conditionVO.deleted}" +
            " ORDER BY ID DESC" +
            " LIMIT :pageInfo.offset,:pageInfo.pageSize")
    List<MqServiceInfoVO> selectListByCondition(@SQLParam("conditionVO") MqServiceInfoConditionVO conditionVO,
                                                @SQLParam("pageInfo") Pageable pageInfo);

    @SQL("INSERT INTO MQ_SERVICE_INFO" +
            " (" + ALL_FIELD + ")" +
            " VALUES" +
            " (null, :vo.serviceName, :vo.serviceDesc, :vo.groupId, :vo.groupName, :vo.deleted, NOW(), NOW())")
    void saveByVO(@SQLParam("vo") MqServiceInfoVO serviceInfoVO);

    @SQL("INSERT INTO MQ_SERVICE_INFO" +
            " (" + ALL_FIELD + ")" +
            " VALUES" +
            " #for(vo in :voList){(NULL, :vo.serviceName, :vo.serviceDesc, :vo.groupId, :vo.groupName, :vo.deleted, NOW(), NOW())##(:vo.comma)}")
    void saveMqServiceInfoList(@SQLParam("voList") Collection<MqServiceInfoVO> voList);

    @SQL("UPDATE MQ_SERVICE_INFO" +
            " SET" +
            " MODIFY_TIME = NOW()" +
            " #if(:conditionVO.clearGroup == 1){, GROUP_ID = -1}" +
            " #if(:conditionVO.clearGroup == 1){, GROUP_NAME = ''}" +
            " #if(:conditionVO.clearGroup == 0){, GROUP_NAME = :conditionVO.groupName}" +
            " WHERE GROUP_ID = :conditionVO.groupId")
    Integer updateGroup(@SQLParam("conditionVO") MqServiceInfoConditionVO conditionVO);

    @SQL("UPDATE MQ_SERVICE_INFO" +
            " SET" +
            " SERVICE_NAME = :vo.serviceName," +
            " SERVICE_DESC = :vo.serviceDesc," +
            " GROUP_ID = :vo.groupId," +
            " GROUP_NAME = :vo.groupName," +
            " DELETED = :vo.deleted" +
            " WHERE ID = :vo.id")
    Integer updateById(@SQLParam("vo") MqServiceInfoVO serviceInfoVO);

    @SQL("UPDATE MQ_SERVICE_INFO" +
            " SET" +
            " SERVICE_DESC = :vo.serviceDesc," +
            " GROUP_ID = :vo.groupId," +
            " GROUP_NAME = :vo.groupName," +
            " DELETED = :vo.deleted" +
            " WHERE SERVICE_NAME = :vo.serviceName")
    Integer updateByServiceName(@SQLParam("vo") MqServiceInfoVO serviceInfoVO);

    @SQL("UPDATE MQ_SERVICE_INFO" +
            " SET" +
            " DELETED = 1" +
            " WHERE ID = :serviceId")
    void deleteServiceInfo(@SQLParam("serviceId") Long serviceId);
}
