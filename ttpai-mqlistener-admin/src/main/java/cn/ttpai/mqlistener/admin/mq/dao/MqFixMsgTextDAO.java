package cn.ttpai.mqlistener.admin.mq.dao;

import cn.ttpai.mqlistener.admin.mq.model.MqFixMsgTextVO;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/**
 * MQ修复消息文本DAO
 *
 * @author jiayuan.su
 */
@DAO
public interface MqFixMsgTextDAO {

    String ALL_FIELD = "ID, MSG_TEXT";

    @ReturnGeneratedKeys
    @SQL("INSERT INTO MQ_FIX_MSG_TEXT" +
            " (" + ALL_FIELD + ")" +
            " VALUES" +
            " (NULL, :vo.msgText)")
    Long saveItem(@SQLParam("vo") MqFixMsgTextVO vo);

    @SQL("UPDATE MQ_FIX_MSG_TEXT" +
            " SET" +
            " MSG_TEXT = :vo.msgText" +
            " WHERE ID = :vo.id")
    void updateItem(@SQLParam("vo") MqFixMsgTextVO vo);
}
