package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * MQ修复消息DAO
 * <p>
 * @author jiayuan.su
 */
@DAO
public interface MqFixMsgDAO {

    String ALL_PREFIX_FILED = "m.ID, m.FIX_ID, m.MSG_TEXT_ID, m.ORDER_NUM, m.FIX_MSG_STATUS, m.FIX_REMARK, m.DELETED, m.CREATE_TIME, m.MODIFY_TIME";
    String ALL_FILED = "ID, FIX_ID, MSG_TEXT_ID, ORDER_NUM, FIX_MSG_STATUS, FIX_REMARK, DELETED, CREATE_TIME, MODIFY_TIME";

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_FIX_MSG" +
            " WHERE DELETED = 0" +
            " AND FIX_ID = :queryVO.fixId" +
            " #if(:queryVO.id != null){AND ID = :queryVO.id}" +
            " #if(:queryVO.fixMsgStatus != null){AND FIX_MSG_STATUS = :queryVO.fixMsgStatus}")
    Long selectCount(@SQLParam("queryVO") MqFixMsgVO queryVO);

    @SQL("SELECT " + ALL_PREFIX_FILED + ", mt.MSG_TEXT" +
            " FROM MQ_FIX_MSG m" +
            " LEFT JOIN MQ_FIX_MSG_TEXT mt" +
            " ON m.MSG_TEXT_ID = mt.ID" +
            " WHERE m.DELETED = 0" +
            " AND m.FIX_ID = :queryVO.fixId" +
            " #if(:queryVO.id != null){AND m.ID = :queryVO.id}" +
            " #if(:queryVO.fixMsgStatus != null){AND m.FIX_MSG_STATUS = :queryVO.fixMsgStatus}" +
            " ORDER BY m.ORDER_NUM ASC" +
            " LIMIT :pageInfo.offset, :pageInfo.pageSize")
    List<MqFixMsgVO> selectItemList(@SQLParam("queryVO") MqFixMsgVO queryVO, @SQLParam("pageInfo") Pageable pageInfo);

    @SQL("SELECT " + ALL_PREFIX_FILED + ", mt.MSG_TEXT" +
            " FROM MQ_FIX_MSG m" +
            " LEFT JOIN MQ_FIX_MSG_TEXT mt" +
            " ON m.MSG_TEXT_ID = mt.ID" +
            " WHERE m.ID = :id" +
            " AND m.DELETED = 0")
    MqFixMsgVO selectItemWithMsgText(@SQLParam("id") Long id);

    @SQL("SELECT " + ALL_FILED +
            " FROM MQ_FIX_MSG" +
            " WHERE ID = :id" +
            " AND DELETED = 0")
    MqFixMsgVO selectItemNoMsgText(@SQLParam("id") Long id);

    @SQL("SELECT MAX(ORDER_NUM)" +
            " FROM MQ_FIX_MSG" +
            " WHERE FIX_ID = :fixId" +
            " AND DELETED = 0")
    Integer selectMaxOrderNum(@SQLParam("fixId") Long fixId);

    @SQL("INSERT INTO" +
            " MQ_FIX_MSG" +
            " (" + ALL_FILED + ")" +
            " VALUES" +
            " (NULL, :saveVO.fixId, :saveVO.msgTextId, :saveVO.orderNum, :saveVO.fixMsgStatus, :saveVO.fixRemark, :saveVO.deleted, NOW(), NOW())")
    void saveItem(@SQLParam("saveVO") MqFixMsgVO saveVO);

    @SQL("UPDATE MQ_FIX_MSG" +
            " SET" +
            " ORDER_NUM = ORDER_NUM + 1" +
            " WHERE FIX_ID = :updateVO.fixId" +
            " AND ORDER_NUM >= :updateVO.orderNum" +
            " AND DELETED = 0")
    void updateOrder(@SQLParam("updateVO") MqFixMsgVO updateVO);

    @SQL("UPDATE MQ_FIX_MSG" +
            " SET" +
            " MODIFY_TIME = NOW()" +
            " #if(:updateVO.orderNum != null){, ORDER_NUM = :updateVO.orderNum}" +
            " #if(:updateVO.fixMsgStatus != null){, FIX_MSG_STATUS = :updateVO.fixMsgStatus}" +
            " #if(:updateVO.fixRemark != null){, FIX_REMARK = :updateVO.fixRemark}" +
            " WHERE ID = :updateVO.id" +
            " AND DELETED = 0")
    void updateItem(@SQLParam("updateVO") MqFixMsgVO updateVO);

    @SQL("UPDATE MQ_FIX_MSG" +
            " SET" +
            " DELETED = 1" +
            " WHERE ID = :id" +
            " AND FIX_ID = :fixId" +
            " AND FIX_MSG_STATUS = 0")
    void deleteItemById(@SQLParam("fixId") Long fixId, @SQLParam("id") Long id);

    @SQL("UPDATE MQ_FIX_MSG" +
            " SET" +
            " DELETED = 1" +
            " WHERE ID IN (:ids)" +
            " AND FIX_ID = :fixId" +
            " AND FIX_MSG_STATUS = 0")
    void deleteItemByIdCollection(@SQLParam("fixId") Long fixId, @SQLParam("ids") Collection<Long> ids);

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_FIX_MSG" +
            " WHERE FIX_ID = :fixId" +
            " #if(:fixMsgStatus != null){AND FIX_MSG_STATUS = :fixMsgStatus}" +
            " AND DELETED = 0")
    Integer selectFixSummary(@SQLParam("fixId") Long fixId, @SQLParam("fixMsgStatus") Integer fixMsgStatus);
}
