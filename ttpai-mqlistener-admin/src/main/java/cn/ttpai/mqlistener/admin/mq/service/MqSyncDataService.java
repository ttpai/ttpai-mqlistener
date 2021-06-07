package cn.ttpai.mqlistener.admin.mq.service;

import cn.ttpai.mqlistener.admin.common.enums.ErrorInfoEnum;
import cn.ttpai.mqlistener.admin.common.util.ExceptionUtil;
import cn.ttpai.mqlistener.admin.mq.enums.MQBaseConfigTypeEnum;
import cn.ttpai.mqlistener.admin.mq.model.MqBaseConfigVO;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MQ同步数据service
 *
 * @author jiayuan.su
 */
@Service
public class MqSyncDataService {

    @Autowired
    private MqBaseConfigService baseConfigService;

    /**
      * 查询同步开启是否开启
      *
      * @return boolean true-开启了, false-未开启
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public boolean selectSyncOpenIsOpen() {
        List<MqBaseConfigVO> baseConfigList = baseConfigService.selectMqBaseConfigList(MQBaseConfigTypeEnum.ZK_TO_DB.getCode());
        if (CollectionUtils.isEmpty(baseConfigList) || baseConfigList.size() > 1) {
            throw ExceptionUtil.buildBusinessException(ErrorInfoEnum.SYNC_OPEN_CONFIG_ERROR);
        }

        String paramValue = baseConfigList.get(0).getParamValue();
        return Objects.equals("1", paramValue);
    }

    /**
      * 更新同步开启为关闭
      *
      * @author jiayuan.su
      * @date 2020年07月27日
      */
    public void updateSyncOpen2Close() {
        MqBaseConfigVO baseConfigVO = new MqBaseConfigVO();
        baseConfigVO.setConfigType(MQBaseConfigTypeEnum.ZK_TO_DB.getCode());
        baseConfigVO.setParamName("syncOpen");
        baseConfigVO.setParamValue("0");

        List<MqBaseConfigVO> baseConfigVOList = new ArrayList<>();
        baseConfigVOList.add(baseConfigVO);

        baseConfigService.updateMqBaseConfigWithList(baseConfigVOList);
    }
}
