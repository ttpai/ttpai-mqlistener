package cn.ttpai.mqlistener.admin.common.enums;

/**
 * 错误信息枚举
 * <p>
 * @author jiayuan.su
 */
public enum ErrorInfoEnum {
    /**
     * 系统异常
     */
    SYSTEM_ERROR("600", "系统异常"),
    /**
     * 服务名为空
     */
    SERVICE_NAME_BLANK("601", "服务名为空"),
    /**
     * 分页信息为空
     */
    PAGE_INFO_NULL("602", "分页信息为空"),
    /**
     * 系统异常
     */
    NOW_PAGE_ILLEGAL("603", "当前页非法"),
    /**
     * 页大小非法
     */
    PAGE_SIZE_ILLEGAL("604", "页大小非法"),
    /**
     * 同步数据开关配置错误
     */
    SYNC_OPEN_CONFIG_ERROR("605", "同步数据开关配置错误"),
    /**
     * 该服务不存在
     */
    SERVICE_NOT_EXIST("606", "该服务不存在"),
    /**
     * 组ID为空
     */
    GROUP_ID_IS_NULL("607", "组ID为空"),
    /**
     * 组名称为空
     */
    GROUP_NAME_IS_NULL("608", "组名称为空"),
    /**
     * 组不存在
     */
    GROUP_NOT_EXIST("609", "组不存在"),
    /**
     * 已存在该服务
     */
    SERVICE_ALREADY_HAVE("610", "已存在该服务"),
    /**
     * 已存在该组
     */
    GROUP_ALREADY_HAVE("611", "已存在该组"),
    /**
     * 项目名参数缺失
     */
    PROJECT_NAME_BLANK("612", "项目名参数缺失"),
    /**
     * 项目名对应数据找不到
     */
    PROJECT_DATA_NOT_FOUND("613", "项目名对应数据找不到"),
    /**
     * 监听器参数缺失
     */
    LISTENER_NAME_BLANK("614", "监听器参数缺失"),
    /**
     * 监听器配置参数缺失
     */
    LISTENER_CONFIG_NULL("615", "监听器配置参数缺失"),
    /**
     * 监听器对应配置数据不存在
     */
    LISTENER_CONFIG_MISSING("616", "监听器对应配置数据不存在"),
    /**
     * 监听器描述信息缺失
     */
    LISTENER_DESC_BLANK("617", "监听器描述信息缺失"),
    /**
     * 监听器对应描述数据不存在
     */
    LISTENER_DESC_MISSING("618", "监听器对应描述数据不存在"),
    /**
     * 监听器管理员信息缺失
     */
    LISTENER_MANAGER_BLANK("619", "监听器管理员信息缺失"),
    /**
     * 监听器对应管理员数据不存在
     */
    LISTENER_MANAGER_MISSING("620", "监听器对应管理员数据不存在"),
    /**
     * 实例名称/实例启用数据缺失
     */
    INSTANCE_NAME_OR_DATA_MISSING("621", "实例名称/实例启用数据缺失"),
    /**
     * 实例数据不存在
     */
    INSTANCE_DATA_MISSING("622", "实例数据不存在"),
    /**
     * 监听器配置为空时不能启动
     */
    LISTENER_CONFIG_BLANK_CANNOT_START("623", "监听器配置为空时不能启动"),
    /**
     * 监听器描述为空时不能启动
     */
    LISTENER_DESC_BLANK_CANNOT_START("624", "监听器描述为空时不能启动"),
    /**
     * 监听器管理员为空时不能启动
     */
    LISTENER_MANAGER_BLANK_CANNOT_START("625", "监听器管理员为空时不能启动"),
    /**
     * 实例名称缺失
     */
    INSTANCE_DATA_BLANK("626", "实例名称缺失"),
    /**
     * 监听器路径非法
     */
    LISTENER_PATH_ILLEGAL("627", "监听器路径非法"),
    /**
     * 实例是否在运行数据丢失
     */
    INSTANCE_IN_USE_MISSING("628", "实例是否在运行数据丢失"),
    /**
     * 修复ID为空
     */
    FIX_ID_IS_NULL("629", "修复ID为空"),
    /**
     * 修复数据不存在
     */
    FIX_DATA_NOT_EXIST("630", "修复数据不存在"),
    /**
     * 修复消息ID为空
     */
    FIX_MSG_ID_IS_NULL("631", "修复消息ID为空"),
    /**
     * 修复消息数据不存在
     */
    FIX_MSG_DATA_NOT_EXIST("632", "修复消息数据不存在"),
    /**
     * 排序数字为空或小于1
     */
    ORDER_NUM_ILLEGAL("633", "排序数字为空或小于1"),
    /**
     * 修复数据状态不正确
     */
    FIX_STATUS_ILLEGAL("634", "修复数据状态不正确"),
    /**
     * 修复数据是结束状态
     */
    FIX_DATA_IS_END("635", "修复数据是结束状态"),
    /**
     * 添加修复消息类型不正确
     */
    ADD_FIX_MSG_TYPE_WRONG("636", "添加修复消息类型不正确"),
    /**
     * 修复备注为空
     */
    FIX_REMARK_IS_BLANK("637", "修复备注为空"),
    /**
     * 修复消息文本为空
     */
    FIX_MSG_TEXT_IS_BLANK("638", "修复消息文本为空"),
    /**
     * 修复名称为空
     */
    FIX_NAME_IS_BLANK("639", "修复名称为空"),
    /**
     * RabbitMQ类型不正确
     */
    RABBIT_SERVER_TYPE_WRONG("640", "RabbitMQ类型不正确"),
    /**
     * VirtualHost为空
     */
    VIRTUAL_HOST_IS_BLANK("641", "VirtualHost为空"),
    /**
     * 队列名为空
     */
    QUEUE_NAME_IS_BLANK("642", "队列名为空"),
    /**
     * 请求RabbitMQ返回结果为空
     */
    REQUIST_RABBIT_MQ_RETURN_BLANK("643", "请求RabbitMQ返回结果为空"),
    /**
     * RabbitMQ返回结果不为JSON
     */
    RABBIT_MQ_RETURN_NOT_JSON("644", "RabbitMQ返回结果不为JSON"),
    /**
     * 发送消息到队列返回结果不为成功
     */
    MSG_TO_QUEUE_RETURN_NOT_SUCCESS("645", "发送消息到队列返回结果不为成功"),
    /**
     * 修复消息不是待修复状态
     */
    FIX_MSG_NOT_WAIT_FIX_STATUS("646", "修复消息不是待修复状态"),
    /**
     * 消息获取数量不正确
     */
    GET_NUM_WRONG("647", "消息获取数量不正确"),
    /**
     * 请求RabbitMQ发生异常
     */
    REQUIST_RABBIT_MQ_ERROR("648", "请求RabbitMQ发生异常"),
    /**
     * 修复消息列表为空
     */
    FIX_MSG_LIST_EMPTY("649", "修复消息列表为空"),
    /**
     * 修复消息状态不正确
     */
    FIX_MSG_STATUS_ILLEGAL("650", "修复消息状态不正确"),
    /**
     * 修复中或修复异常的消息才能变更状态
     */
    IN_FIX_OR_FIX_ERROR_MSG_CAN_UPDATE("651", "修复中或修复异常的消息才能变更状态");

    private String code;
    private String message;

    ErrorInfoEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
