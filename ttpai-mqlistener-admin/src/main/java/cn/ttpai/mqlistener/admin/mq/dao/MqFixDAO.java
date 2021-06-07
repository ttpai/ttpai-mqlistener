package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqFixVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * MQ修复DAO
 *
 * @author jiayuan.su
 */
@DAO
public interface MqFixDAO {

    String ALL_FIELD = "ID, FIX_NAME, CHARGE_PERSON, FIX_STATUS, FIX_REMARK, CREATE_TIME, MODIFY_TIME";

    @SQL("SELECT COUNT(1)" +
            " FROM MQ_FIX" +
            " WHERE 1 = 1" +
            " #if(:queryVO.chargePersonLike != null){AND CHARGE_PERSON LIKE :queryVO.chargePersonLike}" +
            " #if(:queryVO.fixStatus != null){AND FIX_STATUS = :queryVO.fixStatus}")
    Long selectCount(@SQLParam("queryVO") MqFixVO queryVO);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_FIX" +
            " WHERE 1 = 1" +
            " #if(:queryVO.chargePersonLike != null){AND CHARGE_PERSON LIKE :queryVO.chargePersonLike}" +
            " #if(:queryVO.fixStatus != null){AND FIX_STATUS = :queryVO.fixStatus}" +
            " ORDER BY CREATE_TIME DESC" +
            " LIMIT :pageInfo.offset, :pageInfo.pageSize")
    List<MqFixVO> selectItemList(@SQLParam("queryVO") MqFixVO queryVO, @SQLParam("pageInfo") Pageable pageInfo);

    @SQL("SELECT " + ALL_FIELD +
            " FROM MQ_FIX" +
            " WHERE ID = :id")
    MqFixVO selectItem(@SQLParam("id") Long id);

    @SQL("INSERT INTO MQ_FIX" +
            " (" + ALL_FIELD + ")" +
            " VALUES" +
            " (NULL, :saveVO.fixName, :saveVO.chargePerson, :saveVO.fixStatus, :saveVO.fixRemark, NOW(), NOW())")
    void saveItem(@SQLParam("saveVO") MqFixVO saveVO);

    @SQL("UPDATE MQ_FIX" +
            " SET" +
            " MODIFY_TIME = NOW()" +
            " #if(:updateVO.fixName != null){, FIX_NAME = :updateVO.fixName}" +
            " #if(:updateVO.chargePerson != null){, CHARGE_PERSON = :updateVO.chargePerson}" +
            " #if(:updateVO.fixStatus != null){, FIX_STATUS = :updateVO.fixStatus}" +
            " #if(:updateVO.fixRemark != null){, FIX_REMARK = :updateVO.fixRemark}" +
            " WHERE ID = :updateVO.id")
    void updateItem(@SQLParam("updateVO") MqFixVO updateVO);
}
