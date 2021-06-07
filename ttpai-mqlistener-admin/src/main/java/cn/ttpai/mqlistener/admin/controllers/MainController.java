package cn.ttpai.mqlistener.admin.controllers;

import cn.ttpai.mqlistener.admin.common.annotation.NotBlank;
import cn.ttpai.mqlistener.admin.common.base.BaseController;
import cn.ttpai.mqlistener.admin.common.util.JsonUtil;
import cn.ttpai.mqlistener.admin.common.util.ResultJson;
import cn.ttpai.mqlistener.admin.common.util.RoseReturn;
import cn.ttpai.mqlistener.admin.common.util.StringUtil;
import cn.ttpai.mqlistener.admin.zoo.enums.ListenerStatusEnum;
import cn.ttpai.mqlistener.admin.zoo.enums.ProcessTypeEnum;
import cn.ttpai.mqlistener.admin.zoo.model.ListenerConfig;
import cn.ttpai.mqlistener.admin.zoo.model.ListenerInfo;
import cn.ttpai.mqlistener.admin.zoo.model.NodeVO;
import cn.ttpai.mqlistener.admin.zoo.service.ZkService;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.paoding.rose.web.var.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

/**
 * 组controller
 *
 * @author jiayuan.su
 */
@Path("")
public class MainController extends BaseController {

    @Autowired
    private ZkService zkService;

    @Get("/getRealList")
    public String getRealList(@NotBlank @Param("severPath") String severPath, Model model) {
        List<String> listenerList = zkService.selectListenerList(severPath);

        model.add("path", severPath);
        model.add("realList", listenerList);

        return "mqListener/realList";
    }

    @Get("/getDetails")
    public String getDetails(@NotBlank @Param("path") String path, Model model) {
        ListenerInfo listenerInfo = zkService.selectListenerInfo(path);
        listenerInfo = (null != listenerInfo ? listenerInfo : new ListenerInfo());

        if (null != listenerInfo.getListenerConfig()) {
            model.add("listenerConfig", listenerInfo.getListenerConfig());
        }
        model.add("mqDesc", StringUtil.isNotBlank(listenerInfo.getDesc()) ? listenerInfo.getDesc() : "");
        model.add("queue", StringUtil.isNotBlank(listenerInfo.getQueue()) ? listenerInfo.getQueue() : "");
        model.add("manager", StringUtil.isNotBlank(listenerInfo.getManager()) ? listenerInfo.getManager() : "");
        model.add("path", path);

        return "mqListener/mq_details";
    }

    @Get("/amendConfig")
    @Post("/amendConfig")
    public String amendConfig(@NotBlank @Param("path") String path, @RequestBody ListenerConfig listenerConfig) {
        String msg = checkListenerConfig(listenerConfig);
        if (StringUtil.isNotBlank(msg)) {
            return RoseReturn.json(JsonUtil.toJsonString(new ResultJson("-1", msg)));
        }

        zkService.saveListenerConfig(path, listenerConfig);

        return RoseReturn.json(JsonUtil.toJsonString(new ResultJson("0", "保存成功")));
    }

    @Get("/amendDesc")
    @Post("/amendDesc")
    public String amendDesc(@NotBlank @Param("path") String path, @NotBlank @Param("desc") String data) {
        zkService.saveListenerDesc(path, data);

        return RoseReturn.json(JsonUtil.toJsonString(new ResultJson("0", "保存成功")));
    }

    @Get("/amendManager")
    @Post("/amendManager")
    public String amendManager(@NotBlank @Param("path") String path, @NotBlank @Param("manager") String data) {
        zkService.saveListenerManager(path, data);

        return RoseReturn.json(JsonUtil.toJsonString(new ResultJson("0", "保存成功")));
    }

    @Get("/getNodeList")
    public String getNodeList(@NotBlank @Param("path") String path, Model model) {
        List<NodeVO> nodeVOList = zkService.selectListenerInstanceList(path);

        model.add("nodeVOList", nodeVOList);
        model.add("path", path);

        return "mqListener/nodeList";
    }

    @Get("/stopNode")
    public String stopNode(@NotBlank @Param("path") String path, @NotBlank @Param("nodePath") String nodePath) {
        zkService.updateListenerInstanceStatus(path, nodePath, ListenerStatusEnum.STOP.getCode());

        return "r:/getNodeList?path=" + path;
    }

    @Get("/startNode")
    public String startNode(@NotBlank @Param("path") String path, @NotBlank @Param("nodePath") String nodePath) {
        zkService.updateListenerInstanceStatus(path, nodePath, ListenerStatusEnum.START.getCode());

        return "r:/getNodeList?path=" + path;
    }

    @Get("/deleteInstance")
    public String deleteInstance(@NotBlank @Param("path") String path, @NotBlank @Param("nodePath") String nodePath) {
        zkService.deleteInstance(path, nodePath);

        return "r:/getNodeList?path=" + path;
    }

    @Get("/deleteListener")
    public String deleteListener(@NotBlank @Param("path") String path, @NotBlank @Param("beanId") String beanId) {
        zkService.deleteListener(path, beanId);

        return "r:/getRealList?severPath="+path;
    }

    /**
     * 校验前台传入的配置参数
     */
    private String checkListenerConfig(ListenerConfig listenerConfig) {
        if (null == listenerConfig) {
            return "参数未接收到";
        }

        if (Objects.isNull(listenerConfig.getConcurrentConsumers()) || listenerConfig.getConcurrentConsumers() < 1) {
            return "并发消费数不正确";
        }

        if (Objects.isNull(listenerConfig.getPrefetchCount()) || listenerConfig.getPrefetchCount() < 1) {
            return "预抓取消息数不正确";
        }

        if (Objects.isNull(listenerConfig.getConsumeIntervalMs()) || listenerConfig.getConsumeIntervalMs() < 0) {
            return "消费间隔毫秒数不正确";
        }

        Integer processType = listenerConfig.getProcessType();
        if (null == processType) {
            return "处理类型不能为空";
        }

        ProcessTypeEnum processTypeEnum = ProcessTypeEnum.getEnum(processType);
        if (null == processTypeEnum) {
            return "处理类型未找到";
        }

        switch (processTypeEnum) {
            case EXCEPTION_QUEUE:
                if (StringUtil.isBlank(listenerConfig.getExceptionQueue())) {
                    return "异常队列不能为空";
                }
                listenerConfig.setFailLimit(null);
                listenerConfig.setIsReturnHead(null);
                break;
            case REPUT_QUEUE:
                if (Objects.isNull(listenerConfig.getIsReturnHead())) {
                    return "队尾/队头不能为空";
                }
                listenerConfig.setFailLimit(null);
                listenerConfig.setExceptionQueue(null);
                break;
            case REPUT_QUEUE_COUNT_DISCARD:
                if (Objects.isNull(listenerConfig.getFailLimit()) || listenerConfig.getFailLimit() < 1) {
                    return "消费失败次数不正确";
                }
                listenerConfig.setExceptionQueue(null);
                listenerConfig.setIsReturnHead(null);
                break;
            case REPUT_QUEUE_COUNT_EXCEPTION_QUEUE:
                if (Objects.isNull(listenerConfig.getFailLimit()) || listenerConfig.getFailLimit() < 1) {
                    return "消费失败次数不正确";
                }
                if (StringUtil.isBlank(listenerConfig.getExceptionQueue())) {
                    return "异常队列不能为空";
                }
                listenerConfig.setIsReturnHead(null);
                break;
            default:
                logger.error("warn: 该处理类型({})不支持", processTypeEnum.getCode());
                return "该处理类型不支持";
        }

        return "";
    }
}
