package cn.ttpai.mqlistener.admin.rabbit.service;

import cn.ttpai.mqlistener.admin.common.component.net.HttpClientService;
import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.rabbit.constant.RabbitConst;
import cn.ttpai.mqlistener.admin.rabbit.enums.RabbitServerTypeEnum;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitBasisInfoVO;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitGetMsgVO;
import cn.ttpai.mqlistener.admin.rabbit.model.RabbitSendDataVO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Rabbit service
 *
 * @author jiayuan.su
 */
@Service
public class RabbitService {
    private static final Logger logger = LoggerFactory.getLogger(RabbitService.class);

    @Value("${rabbit.app.admin.ip}")
    private String appIp;
    @Value("${rabbit.app.admin.port}")
    private String appPort;
    @Value("${rabbit.app.admin.username}")
    private String appUsername;
    @Value("${rabbit.app.admin.password}")
    private String appPassword;

    @Value("${rabbit.soa.admin.ip}")
    private String soaIp;
    @Value("${rabbit.soa.admin.port}")
    private String soaPort;
    @Value("${rabbit.soa.admin.username}")
    private String soaUsername;
    @Value("${rabbit.soa.admin.password}")
    private String soaPassword;

    @Autowired
    private HttpClientService httpClientService;

    /**
      * 根据服务器类型获取RabbitMQ基本信息VO
      *
      * @param serverType 服务器类型
      * @return RabbitBasisInfoVO RabbitMQ基本信息VO
      * @author jiayuan.su
      * @date 2020年09月16日
      */
    public RabbitBasisInfoVO getBasisInfoVO(Integer serverType) {
        if (Objects.equals(serverType, RabbitServerTypeEnum.APP.getType())) {
            return new RabbitBasisInfoVO(appIp + ":" + appPort, appUsername, appPassword);
        }
        if (Objects.equals(serverType, RabbitServerTypeEnum.SOA.getType())) {
            return new RabbitBasisInfoVO(soaIp + ":" + soaPort, soaUsername, soaPassword);
        }
        return null;
    }

    /**
      * 发送消息到队列
      *
      * @param sendDataVO 必要参数
      * @param basisInfoVO 基本参数
      * @author jiayuan.su
      * @date 2020年09月14日
      */
    public void sendMsg2Queue(RabbitSendDataVO sendDataVO, RabbitBasisInfoVO basisInfoVO) throws IOException {
        String url = String.format(RabbitConst.Url.PUBLISH_TPLT, basisInfoVO.getIpPort(), sendDataVO.getVhost());

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("properties", Collections.emptyMap());
        bodyMap.put("routing_key", sendDataVO.getQueueName());
        bodyMap.put("payload", sendDataVO.getMsgText());
        bodyMap.put("payload_encoding", "string");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");

        String result = httpClientService.post(url, JsonUtil.toJson(bodyMap), headerMap);
        logger.info("发送消息到队列返回结果记录. 参数: fixMsgId={}, serverType={}, vhost={}, queueName={}. 结果: {}.",
                sendDataVO.getFixMsgId(), sendDataVO.getServerType(), sendDataVO.getVhost(), sendDataVO.getQueueName(), result);

        if (StringUtils.isBlank(result)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.REQUIST_RABBIT_MQ_RETURN_BLANK);
        }
        Map<String, Object> resultMap = null;
        try {
            resultMap = JsonUtil.toObject(result, Map.class);
        } catch (Exception e) {
            logger.error("发送消息到队列返回结果不为JSON. 参数: fixMsgId={}, serverType={}, vhost={}, queueName={}. 结果: {}.",
                    sendDataVO.getFixMsgId(), sendDataVO.getServerType(), sendDataVO.getVhost(), sendDataVO.getQueueName(), result);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.RABBIT_MQ_RETURN_NOT_JSON);
        }
        if (!Objects.equals(true, resultMap.get("routed"))) {
            logger.error("发送消息到队列返回结果不为成功. 参数: fixMsgId={}, serverType={}, vhost={}, queueName={}. 结果: {}.",
                    sendDataVO.getFixMsgId(), sendDataVO.getServerType(), sendDataVO.getVhost(), sendDataVO.getQueueName(), result);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.MSG_TO_QUEUE_RETURN_NOT_SUCCESS);
        }
    }

    /**
      * 从队列中获取消息文本列表
      *
      * @param getMsgVO 必要参数
      * @param basisInfoVO 基本参数
      * @return List<String> 消息列表
      * @author jiayuan.su
      * @date 2020年09月16日
      */
    public List<String> getMsgTextList(RabbitGetMsgVO getMsgVO, RabbitBasisInfoVO basisInfoVO) {
        String url = String.format(RabbitConst.Url.GET_MSG_TPLT, basisInfoVO.getIpPort(), getMsgVO.getVhost(), getMsgVO.getQueueName());

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("count", getMsgVO.getGetNum());
        bodyMap.put("ackmode", "ack_requeue_true");
        bodyMap.put("encoding", "auto");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");

        String result = null;
        try {
            result = httpClientService.post(url, JsonUtil.toJson(bodyMap), headerMap);
            logger.info("从队列中获取消息返回结果记录. 参数: getMsgVO={}. 结果: {}.", getMsgVO,
                    StringUtils.isNotBlank(result) && result.length() > 300 ? result.substring(0, 299) : result);
        } catch (Exception e) {
            logger.error("从队列中获取消息请求异常. 参数: getMsgVO={}. 异常详情: ", getMsgVO, e);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.REQUIST_RABBIT_MQ_ERROR);
        }

        if (StringUtils.isBlank(result)) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.REQUIST_RABBIT_MQ_RETURN_BLANK);
        }
        List<Map<String, Object>> mapResultList = null;
        try {
            mapResultList = JsonUtil.toObject(result, new JsonUtil.AbstractJsonReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            logger.error("从队列中获取消息返回结果不为JSON. 参数: getMsgVO={}. 结果: {}.", getMsgVO,
                    StringUtils.isNotBlank(result) && result.length() > 300 ? result.substring(0, 299) : result);
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.RABBIT_MQ_RETURN_NOT_JSON);
        }

        List<String> msgTextList = new ArrayList<>();
        for (Map<String, Object> mapResult : mapResultList) {
            msgTextList.add((String) mapResult.get("payload"));
        }
        return msgTextList;
    }
}
