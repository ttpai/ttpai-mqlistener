<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <!--禁止浏览器在本地计算机缓存当前页面-->
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="author" content="天天拍车">
    <meta name="keywords" content="二手车,二手车交易市场,卖车,二手车评估,二手车估价,二手车买卖,二手车帮卖,天天拍车二手车">
    <meta name="description" content="天天拍车,中国领先的二手车交易平台,免费上门检测,上千家车商竞价,确保卖车最高价,极速打款15分钟搞定,卖车卖出新体验！详询拨打10108885">
    <link href="./css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        .fix-alert {
            margin-top: 15px;
            margin-bottom: 15px;
        }
        .fix-alert .fix-alert-tip {
            color: #aa6708;
        }
        .fix-alert .form-group {
            margin-right: 50px;
        }
        .fix-msg-list .form-control {
            width: auto;
        }
        .fix-msg-list .form-inline {
            margin: 20px auto;
        }
        .fix-msg-list .form-group:nth-child(1) {
            padding-left: 10px;
        }
        .fix-msg-list .form-group {
            padding-right: 30px;
        }
        .fix-msg-list .table td {
            word-break: break-all;
        }
        .fix-msg-list .msg-text-td p {
            margin: 0;
            line-height: 1.6;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
        }
        .fix-msg-list .msg-text-td p.truncate + label {
            display: block;
        }
        .fix-msg-list .msg-text-td label {
            font-weight: normal;
            cursor: pointer;
            color: #bbbbbb;
            display: none;
        }
        .fix-msg-list .msg-text-td label:hover {
            color: black;
        }
        .fix-msg-list .msg-text-td label::after {
            content: "显示更多";
        }
        .fix-msg-list .msg-text-td input:checked + p {
            -webkit-line-clamp: unset;
        }
        .fix-msg-list .msg-text-td input:checked ~ label:after {
            content: "收起文本";
        }
        .fix-msg-page {
            overflow: hidden;
        }
        .fix-msg-page .pagination {
            margin: 0 auto 20px;
            padding-right: 15px;
        }
        .add-fix-msg textarea, .edit-fix-msg textarea, .send-fix-msg textarea {
            resize: vertical;
            height: 120px;
        }
        .add-fix-msg .analyse-form-group {
            margin-bottom: 0px;
        }
        .add-fix-msg .analyse-table {
            max-height: 200px;
            overflow-y: auto
        }
        .send-fix-msg textarea.form-control[disabled] {
            cursor: default;
        }
        .batch-send-fix-msg-progress .progress {
            margin-bottom: 0px;
        }
        .batch-send-fix-msg-progress .log-pane {
            margin-bottom: 0px;
            min-height: 150px;
            max-height: 256px;
            overflow-y: auto
        }
    </style>
</head>
<body>
<div class="container-fluid" id="app">
    <div>
        <div class="location-toolbar clearfix">
            <a href="./fixList"  class="btn btn-primary assign-batch">回到修复单列表</a>
        </div>
        <div class="alert alert-success fix-alert" role="alert">
            <h4>
                <span class="fix-alert-tip">
                    <span v-if="fix.fixStatus === 0">修复中, 能被查看&修复</span>
                    <span v-else>已结束, 仅能被查看. </span>
                </span>
            </h4>
            <form class="form-inline">
                <div class="form-group">
                    <label>修复名称: </label>
                    <span v-text="fix.fixName"></span>
                </div>
                <div class="form-group">
                    <label>修复状态: </label>
                    <span v-text="fix.fixStatusText"></span>
                </div>
                <div class="form-group">
                    <label>备注: </label>
                    <span v-text="fix.fixRemark"></span>
                </div>
                <div class="form-group">
                    <label>创建时间: </label>
                    <span v-text="fix.createTime"></span>
                </div>
                <div class="form-group">
                    <label>负责人: </label>
                    <span v-text="fix.chargePerson"></span>
                </div>
            </form>
        </div>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href=".fix-msg-list" aria-controls="profile" role="tab" data-toggle="tab">修复消息列表</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active fix-msg-list">
                <form class="form-inline">
                    <div class="form-group">
                        <label>修复消息ID: </label>
                        <input type="text" class="form-control" v-model="query.id" placeholder="请输入修复消息ID">
                    </div>
                    <div class="form-group">
                        <label>修复状态: </label>
                        <select class="form-control" v-model="query.fixMsgStatus">
                            <option value="">请选择状态</option>
                            <option value="0">待修复</option>
                            <option value="1">修复中</option>
                            <option value="2">修复成功</option>
                            <option value="3">修复异常</option>
                        </select>
                    </div>
                    <button type="button" @click="queryFixMsgHandle" class="btn btn-primary">查询</button>
                    <button type="button" @click="queryClearHandle" class="btn btn-primary">清空条件</button>
                    <button type="button" @click="addFixMsgHandle" v-if="fix.fixStatus === 0" class="btn btn-success">添加</button>
                    <button type="button" @click="batchSendFixMsgHandle" v-if="fix.fixStatus === 0" class="btn btn-success">批量修复</button>
                </form>
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th width="3%">#</th>
                        <th width="3%" v-if="fix.fixStatus === 0"><input type="checkbox" @click="globalCheckHandle" :checked="globalChecked"></th>
                        <th width="5%">消息ID</th>
                        <th width="36%">消息文本</th>
                        <th width="5%">排序</th>
                        <th width="10%">创建时间</th>
                        <th width="7%">修复状态</th>
                        <th width="16%">修复备注</th>
                        <th width="15%" v-if="fix.fixStatus === 0">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(fixMsg, index) in fixMsgList" :key="fixMsg.id">
                        <td v-text="index + 1"></td>
                        <td v-if="fix.fixStatus === 0"><input type="checkbox" @click="checkHandle($event, index)" :checked="fixMsg.checked" v-if="fixMsg.fixMsgStatus === 0"></td>
                        <td v-text="fixMsg.id"></td>
                        <td class="msg-text-td">
                            <input type="checkbox" :id="'toggle' + index" :checked="fixMsg.unfold" style="display: none;"/>
                            <p v-text="fixMsg.msgText" :data-index="index" class="msg-text-p"></p>
                            <label :for="'toggle' + index" class="pull-right"></label>
                        </td>
                        <td v-text="fixMsg.orderNum"></td>
                        <td v-text="$options.filters.date(fixMsg.createTime)"></td>
                        <td v-text="$options.filters.fixMsgStatusText(fixMsg.fixMsgStatus)"></td>
                        <td v-text="fixMsg.fixRemark"></td>
                        <td v-if="fix.fixStatus === 0">
                            <a href="javascript:void(0)" v-if="fixMsg.fixMsgStatus === 0" @click="editFixMsgHandle(fixMsg.id)" class="btn btn-primary">编辑</a>
                            <a href="javascript:void(0)" v-if="fixMsg.fixMsgStatus === 0" @click="deleteFixMsgHandle(fixMsg.id)" class="btn btn-primary">删除</a>
                            <a href="javascript:void(0)" v-if="fixMsg.fixMsgStatus === 0" @click="sendFixMsgHandle(fixMsg.id)" class="btn btn-primary">修复</a>
                            <a href="javascript:void(0)" v-if="fixMsg.fixMsgStatus === 1 || fixMsg.fixMsgStatus === 3" @click="resetFixMsgHandle(fixMsg.id)" class="btn btn-primary">重置</a>
                            <a href="javascript:void(0)" v-if="fixMsg.fixMsgStatus === 1 || fixMsg.fixMsgStatus === 3" @click="setFixMsgSuccessHandle(fixMsg.id)" class="btn btn-primary">置为成功</a>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" class="fix-msg-page">
                    <ul class="pagination pull-right">
                        <li>
                            <a href="javascript:void(0)" @click="prePageHandle" aria-label="Previous">
                                <span aria-hidden="true">上一页</span>
                            </a>
                        </li>
                        <li><a href="javascript:void(0)" v-text="query.pageNum"></a></li>
                        <li>
                            <a href="javascript:void(0)" @click="nextPageHandle" aria-label="Next">
                                <span aria-hidden="true">下一页</span>
                            </a>
                        </li>
                        <li>
                            <span>共<span v-text="pageBar.totalPage"></span>页</span>
                        </li>
                        <li>
                            <span>共<span v-text="pageBar.totalItem"></span>条</span>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
    <!-- 添加修复消息 -->
    <div class="modal fade add-fix-msg" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="addFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加修复消息</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>添加类型: </label>
                            <select class="form-control" v-model="addFixMsgData.addFixMsgType">
                                <option value="">请选择</option>
                                <option value="0">界面输入</option>
                                <option value="1">文本内容</option>
                                <option value="2">远程获取</option>
                            </select>
                        </div>
                        <section v-show="addFixMsgData.addFixMsgType === '0'">
                            <div class="form-group">
                                <label>消息文本: </label>
                                <textarea v-model="addFixMsgData.msgText" class="form-control" placeholder="请输入消息文本"></textarea>
                            </div>
                        </section>
                        <section v-show="addFixMsgData.addFixMsgType === '1'">
                            <div class="form-group">
                                <label>文本内容(##分隔): </label>
                                <textarea v-model="addFixMsgAssist.textContent" @input="addFixMsgTextContentHandle" class="form-control" placeholder="请输入消息文本"></textarea>
                            </div>
                            <div class="form-group analyse-form-group">
                                <label>分析结果<span v-text="'(' + analyseNum + '条)'"></span>: </label>
                                <div class="analyse-table">
                                    <table class="table table-bordered">
                                        <tr v-for="(msgText, index) in addFixMsgAssist.msgTextList" v-key="index">
                                            <td v-text="msgText"></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </section>
                        <section v-show="addFixMsgData.addFixMsgType === '2'">
                            <div class="form-group">
                                <label>选择RabbitMQ: </label>
                                <select class="form-control" v-model="addFixMsgData.serverType">
                                    <option value="">请选择</option>
                                    <option value="0">APP RabbitMQ</option>
                                    <option value="1">SOA RabbitMQ</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>VirtualHost: </label>
                                <input type="text" v-model="addFixMsgData.vhost" class="form-control" placeholder="请输入VirtualHost">
                            </div>
                            <div class="form-group">
                                <label>队列名: </label>
                                <input type="text" v-model="addFixMsgData.queueName" class="form-control" placeholder="请输入队列名">
                            </div>
                            <div class="form-group">
                                <label>数量: </label>
                                <input type="text" v-model="addFixMsgData.getNum" class="form-control" placeholder="请输入数量">
                            </div>
                        </section>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="addFixMsgAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="addFixMsgSubmitHandle" :disabled="addFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 编辑修复消息 -->
    <div class="modal fade edit-fix-msg" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="editFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">编辑修复消息</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>消息文本: </label>
                            <textarea v-model="editFixMsgData.msgText" class="form-control" placeholder="请输入消息文本"></textarea>
                        </div>
                        <div class="form-group">
                            <label>排序数字: </label>
                            <input v-model="editFixMsgData.orderNum" type="text" class="form-control" placeholder="请输入排序数字">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" :disabled="editFixMsgAssist.isInProcess" data-dismiss="modal">取消</button>
                    <button type="button" @click="editFixMsgSubmitHandle" :disabled="editFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 删除修复消息数据 -->
    <div class="modal fade delete-fix-msg" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="deleteFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">删除修复消息</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除吗？</p>
                    <p style="color:#EFAD4D;">此操作不可恢复！</p>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="deleteFixMsgAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="deleteFixMsgSubmitHandle" :disabled="deleteFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 发送消息到队列 -->
    <div class="modal fade send-fix-msg" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="sendFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">发送消息到队列</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>消息文本: </label>
                            <textarea v-text="sendFixMsgAssist.msgText" disabled class="form-control" placeholder="请输入消息文本"></textarea>
                        </div>
                        <div class="form-group">
                            <label>选择RabbitMQ: </label>
                            <select class="form-control" v-model="sendFixMsgData.serverType">
                                <option value="">请选择</option>
                                <option value="0">APP RabbitMQ</option>
                                <option value="1">SOA RabbitMQ</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>VirtualHost: </label>
                            <input type="text" v-model="sendFixMsgData.vhost" class="form-control" placeholder="请输入VirtualHost">
                        </div>
                        <div class="form-group">
                            <label>队列名: </label>
                            <input type="text" v-model="sendFixMsgData.queueName" class="form-control" placeholder="请输入队列名">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="sendFixMsgAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="sendFixMsgSubmitHandle" :disabled="sendFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 重置修复消息数据 -->
    <div class="modal fade reset-fix-msg" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="resetFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">重置修复消息</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要重置吗？</p>
                    <p style="color:#EFAD4D;">此操作不可恢复！</p>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="resetFixMsgAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="resetFixMsgSubmitHandle" :disabled="resetFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 置为成功 -->
    <div class="modal fade set-fix-msg-success" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="setFixMsgSuccessAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">置为成功</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>备注: </label>
                            <input type="text" v-model="setFixMsgSuccessData.fixRemark" class="form-control" placeholder="请输入备注">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="setFixMsgSuccessAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="setFixMsgSuccessSubmitHandle" :disabled="setFixMsgSuccessAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 批量发送消息到队列未选择项时提示 -->
    <div class="modal fade batch-send-fix-msg-tip" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">批量发送消息到队列</h4>
                </div>
                <div class="modal-body">
                    <p>请选择要批量发送到队列的消息！</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 批量发送消息到队列 -->
    <div class="modal fade batch-send-fix-msg" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="batchSendFixMsgAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">批量发送消息到队列</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>选择RabbitMQ: </label>
                            <select class="form-control" v-model="batchSendFixMsgData.serverType">
                                <option value="">请选择</option>
                                <option value="0">APP RabbitMQ</option>
                                <option value="1">SOA RabbitMQ</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>VirtualHost: </label>
                            <input type="text" v-model="batchSendFixMsgData.vhost" class="form-control" placeholder="请输入VirtualHost">
                        </div>
                        <div class="form-group">
                            <label>队列名: </label>
                            <input type="text" v-model="batchSendFixMsgData.queueName" class="form-control" placeholder="请输入队列名">
                        </div>
                        <div class="form-group">
                            <label>异常时是否继续: </label>
                            <select class="form-control" v-model="batchSendFixMsgData.errorContinue">
                                <option value="">请选择</option>
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="batchSendFixMsgAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="batchSendFixMsgSubmitHandle" :disabled="batchSendFixMsgAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 批量发送消息到队列进度 -->
    <div class="modal fade batch-send-fix-msg-progress" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">批量发送消息到队列进度</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>RabbitMQ为: </label>
                            <span>{{batchSendFixMsgData.serverType | serverTypeText}}</span>
                        </div>
                        <div class="form-group">
                            <label>VirtualHost: </label>
                            <span>{{batchSendFixMsgData.vhost}}</span>
                        </div>
                        <div class="form-group">
                            <label>队列名: </label>
                            <span>{{batchSendFixMsgData.queueName}}</span>
                        </div>
                        <div class="form-group">
                            <label>异常时是否继续: </label>
                            <span>{{batchSendFixMsgData.errorContinue | errorContinueText}}</span>
                        </div>
                        <div class="form-group">
                            <hr/>
                        </div>
                        <div class="form-group">
                            <label>进度: </label>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" :style="{width: batchSendFixMsgProgressData.finishRatio}" v-text="batchSendFixMsgProgressData.numRatioText"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>日志: </label>
                            <div class="panel panel-default log-pane">
                                <div class="panel-body">
                                    <p v-for="(log, index) in batchSendFixMsgProgressData.logList" v-key="index" v-html="log"></p>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer" v-show="batchSendFixMsgProgressData.canClose">
                    <button type="button" class="btn btn-success" @click="batchSendFixMsgProgressHandle">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="./js/jquery.min.js"></script>
<script src="./js/bootstrap.min.js"></script>
<script src="./js/vue.min.js"></script>
<script>
    var constObj = {
        url: {
            getFix: "./getFix",
            fixMsgListData: "./fixMsgListData",
            addFixMsg: "./addFixMsg",
            getFixMsg: "./getFixMsg",
            deleteFixMsg: "./deleteFixMsg",
            editFixMsg: "./editFixMsg",
            sendFixMsg: "./sendFixMsg",
            resetFixMsg: "./resetFixMsg",
            setFixMsgSuccess: "./setFixMsgSuccess"
        },
        page: {
            defaultPageSize: 120
        },
        fixMsgStatus: {
            0: "待修复",
            1: "修复中",
            2: "修复成功",
            3: "修复异常"
        },
        serverType: {
            0: "APP RabbitMQ",
            1: "SOA RabbitMQ"
        },
        errorContinue: {
            0: "否",
            1: "是"
        },
        fixStatus: {
            0: "修复中",
            1: "已结束"
        }
    };

    var vm = new Vue(
        {
            el: "#app",
            data: {
                fix: {
                    fixId: "",
                    fixName: "",
                    fixStatus: "",
                    fixStatusText: "",
                    fixRemark: "",
                    createTime: "",
                    chargePerson: ""
                },
                query: {
                    fixId: "",
                    id: "",
                    fixMsgStatus: "",
                    pageNum: 1,
                    pageSize: constObj.page.defaultPageSize
                },
                fixMsgList: [],
                pageBar: {
                    noPrePage: true,
                    noNextPage: true,
                    totalPage: 0,
                    totalItem: 0,
                },
                addFixMsgAssist: {
                    isInProcess: false,
                    textContent: "",
                    msgTextList: []
                },
                addFixMsgData: {
                    fixId: "",
                    addFixMsgType: "",
                    msgText: "",
                    msgTextListStr: "",
                    serverType: "",
                    vhost: "",
                    queueName: "",
                    getNum: ""
                },
                editFixMsgAssist: {
                    isInProcess: false
                },
                editFixMsgData: {
                    id: "",
                    msgText: "",
                    orderNum: ""
                },
                deleteFixMsgAssist: {
                    isInProcess: false
                },
                deleteFixMsgData: {
                    fixId: "",
                    fixMsgId: "",
                },
                sendFixMsgAssist: {
                    msgText: "",
                    isInProcess: false
                },
                sendFixMsgData: {
                    serverType: "",
                    vhost: "",
                    queueName: "",
                    fixMsgId: ""
                },
                resetFixMsgAssist: {
                    isInProcess: false
                },
                resetFixMsgData: {
                    fixMsgId: ""
                },
                setFixMsgSuccessAssist: {
                    isInProcess: false
                },
                setFixMsgSuccessData: {
                    fixMsgId: "",
                    fixRemark: ""
                },
                globalChecked: false,
                batchSendFixMsgAssist: {
                    isInProcess: false
                },
                batchSendFixMsgData: {
                    serverType: "",
                    vhost: "",
                    queueName: "",
                    errorContinue: ""
                },
                batchSendFixMsgProgressData: {
                    finishRatio: "",
                    numRatioText: "",
                    logList: [],
                    canClose: false
                }
            },
            filters: {
                fixMsgStatusText: function(fixMsgStatus) {
                    return constObj.fixMsgStatus[fixMsgStatus];
                },
                date: function(timestamp) {
                    var time = new Date(timestamp);

                    var year = time.getFullYear();
                    var month = time.getMonth() + 1;
                    var date = time.getDate();
                    var hour = time.getHours();
                    var minute = time.getMinutes();
                    var second = time.getSeconds();

                    return year + "-" +
                           (month < 10 ? "0" + month : month) + "-" +
                           (date < 10 ? "0" + date : date) + " " +
                           (hour < 10 ? "0" + hour : hour) + ":" +
                           (minute < 10 ? "0" + minute : minute) + ":" +
                           (second < 10 ? "0" + second : second);
                },
                serverTypeText: function(serverType) {
                    if (!serverType) {
                        return "";
                    }
                    return constObj.serverType[serverType];
                },
                errorContinueText: function(errorContinue) {
                    if (!errorContinue) {
                        return "";
                    }
                    return constObj.errorContinue[errorContinue];
                }
            },
            methods: {
                // 获取地址栏参数
                getUrlParam: function (name) {
                    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
                    var r = window.location.search.substr(1).match(reg);
                    if(r != null) {
                        return unescape(r[2]);
                    }
                    return null;
                },
                setFixId: function() {
                    var fixId = this.getUrlParam("fixId");

                    this.query.fixId = fixId;
                    this.addFixMsgData.fixId = fixId;
                    this.fix.fixId = fixId;
                    this.deleteFixMsgData.fixId = fixId;
                },
                // 获取修复消息信息
                getFix: function() {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.getFix,
                            method: "get",
                            data: {
                                fixId: that.fix.fixId
                            },
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.fix.fixName = data.result.fixName;
                                    that.fix.fixStatus = data.result.fixStatus;
                                    that.fix.fixStatusText = constObj.fixStatus[data.result.fixStatus];
                                    that.fix.fixRemark = data.result.fixRemark;
                                    that.fix.createTime = that.$options.filters.date(data.result.createTime);
                                    that.fix.chargePerson = data.result.chargePerson;
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                // 条件查询
                queryFixMsgHandle: function() {
                    var queryClone = JSON.parse(JSON.stringify(this.query));
                    queryClone.pageNum = 1;
                    this.getFixMsgList(queryClone);
                },
                // 清空条件查询
                queryClearHandle: function() {
                    this.query.id = "";
                    this.query.fixMsgStatus = "";
                    this.query.pageNum = 1;
                    this.getFixMsgList(this.query);
                },
                // 添加修复消息
                addFixMsgHandle: function() {
                    this.addFixMsgAssist.isInProcess = false;
                    this.addFixMsgAssist.textContent = "";
                    this.addFixMsgAssist.msgTextList = [];

                    this.addFixMsgData.addFixMsgType = "";
                    this.addFixMsgData.msgText = "";
                    this.addFixMsgData.msgTextListStr = "";
                    this.addFixMsgData.serverType = "";
                    this.addFixMsgData.vhost = "";
                    this.addFixMsgData.queueName = "";
                    this.addFixMsgData.getNum = "";
                    $(".add-fix-msg").modal("show");
                },
                addFixMsgTextContentHandle: function(e) {
                    this.analyseTextContent(e.target.value);
                },
                // 添加修复消息提交
                addFixMsgSubmitHandle: function() {
                    var that = this;

                    that.addFixMsgAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.addFixMsg,
                            method: "post",
                            data: that.addFixMsgData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.addFixMsgAssist.isInProcess = false;
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".add-fix-msg").modal("hide");
                                }
                            },
                            error: function () {
                                that.addFixMsgAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                // 编辑修复消息
                editFixMsgHandle: function(fixMsgId) {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.getFixMsg,
                            method: "get",
                            data: {
                                fixMsgId: fixMsgId
                            },
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.editFixMsgData.id = fixMsgId;
                                    that.editFixMsgData.msgText = data.result.msgText;
                                    that.editFixMsgData.orderNum = data.result.orderNum;
                                    $(".edit-fix-msg").modal("show");
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                // 编辑修复消息提交
                editFixMsgSubmitHandle: function() {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.editFixMsg,
                            method: "post",
                            data: that.editFixMsgData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".edit-fix-msg").modal("hide");
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                deleteFixMsgHandle: function(fixMsgId) {
                    this.deleteFixMsgData.fixMsgId = fixMsgId;
                    $(".delete-fix-msg").modal("show");
                },
                deleteFixMsgSubmitHandle: function() {
                    var that = this;

                    that.deleteFixMsgAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.deleteFixMsg,
                            method: "post",
                            data: that.deleteFixMsgData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.deleteFixMsgAssist.isInProcess = false;
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".delete-fix-msg").modal("hide");
                                }
                            },
                            error: function () {
                                that.deleteFixMsgAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                sendInit: function() {
                    this.sendFixMsgAssist.msgText = "";
                    this.sendFixMsgAssist.isInProcess = false;

                    this.sendFixMsgData.serverType = "";
                    this.sendFixMsgData.vhost = "";
                    this.sendFixMsgData.queueName = "";
                    this.sendFixMsgData.fixMsgId = "";
                },
                sendFixMsgHandle: function(fixMsgId) {
                    var that = this;

                    that.sendInit();

                    $.ajax(
                        {
                            url: constObj.url.getFixMsg,
                            method: "get",
                            data: {
                                fixMsgId: fixMsgId
                            },
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.sendFixMsgAssist.msgText = data.result.msgText;
                                    that.sendFixMsgData.fixMsgId = fixMsgId;
                                    $(".send-fix-msg").modal("show");
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                sendFixMsgSubmitHandle: function() {
                    var that = this;

                    that.sendFixMsgAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.sendFixMsg,
                            method: "post",
                            data: that.sendFixMsgData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.sendFixMsgAssist.isInProcess = false;
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".send-fix-msg").modal("hide");
                                }
                            },
                            error: function () {
                                that.sendFixMsgAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                resetFixMsgHandle: function(fixMsgId) {
                    this.resetFixMsgData.fixMsgId = fixMsgId;
                    $(".reset-fix-msg").modal("show");
                },
                resetFixMsgSubmitHandle: function() {
                    var that = this;

                    that.resetFixMsgAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.resetFixMsg,
                            method: "post",
                            data: that.resetFixMsgData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.resetFixMsgAssist.isInProcess = false;
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".reset-fix-msg").modal("hide");
                                }
                            },
                            error: function () {
                                that.resetFixMsgAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                setFixMsgSuccessHandle: function(fixMsgId) {
                    this.setFixMsgSuccessData.fixMsgId = fixMsgId;
                    this.setFixMsgSuccessData.fixRemark = "";

                    $(".set-fix-msg-success").modal("show");
                },
                setFixMsgSuccessSubmitHandle: function() {
                    var that = this;

                    that.setFixMsgSuccessAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.setFixMsgSuccess,
                            method: "post",
                            data: that.setFixMsgSuccessData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.setFixMsgSuccessAssist.isInProcess = false;
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixMsgList(that.query);
                                    $(".set-fix-msg-success").modal("hide");
                                }
                            },
                            error: function () {
                                that.setFixMsgSuccessAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                prePageHandle: function() {
                    if (this.pageBar.noPrePage) {
                        return;
                    }

                    var queryClone = JSON.parse(JSON.stringify(this.query));
                    queryClone["pageNum"] = queryClone.pageNum - 1;
                    this.getFixMsgList(queryClone);
                },
                nextPageHandle: function() {
                    if (this.pageBar.noNextPage) {
                        return;
                    }

                    var queryClone = JSON.parse(JSON.stringify(this.query));
                    queryClone["pageNum"] = queryClone.pageNum + 1;
                    this.getFixMsgList(queryClone);
                },
                getFixMsgList: function(queryData) {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.fixMsgListData,
                            method: "get",
                            data: queryData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    data.result.content.forEach(function(item) {
                                        item.checked = false;
                                        item.unfold = false;
                                    });
                                    that.fixMsgList = data.result.content;
                                    that.query.pageNum = data.result.number + 1;
                                    that.query.pageSize = data.result.size;
                                    that.pageBar.totalPage = data.result.totalPages;
                                    that.pageBar.totalItem = data.result.totalElements;
                                    that.pageBar.noPrePage = data.result.first;
                                    that.pageBar.noNextPage = data.result.last;
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                globalCheckHandle: function(e) {
                    this.globalChecked = e.target.checked;
                    this.fixMsgList.forEach(function (fixMsg) {
                        if (fixMsg.fixMsgStatus === 0) {
                            fixMsg.checked = e.target.checked;
                        }
                    });
                },
                checkHandle: function(e, index) {
                    this.fixMsgList[index].checked = e.target.checked;

                    this.globalChecked = this.fixMsgList.every(function(fixMsg) {
                        return fixMsg.checked;
                    });
                },
                batchSendFixMsgHandle: function() {
                    var isHasChecked = this.fixMsgList.some(function(fixMsg) {
                        return fixMsg.checked;
                    });

                    if (!isHasChecked) {
                        $(".batch-send-fix-msg-tip").modal("show");
                    } else {
                        this.batchSendFixMsgData.serverType = "";
                        this.batchSendFixMsgData.vhost = "";
                        this.batchSendFixMsgData.queueName = "";
                        this.batchSendFixMsgData.errorContinue = "";

                        $(".batch-send-fix-msg").modal("show");
                    }
                },
                batchSendFixMsgSubmitHandle: function() {
                    if (!this.batchSendFixMsgData.serverType
                        && !this.batchSendFixMsgData.vhost
                        && !this.batchSendFixMsgData.queueName
                        && !this.batchSendFixMsgData.errorContinue) {
                        return;
                    }

                    $(".batch-send-fix-msg").modal("hide");

                    // -------------- 开始批量处理 --------------
                    // 重置数据
                    this.batchSendFixMsgProgressData.finishRatio = "";
                    this.batchSendFixMsgProgressData.numRatioText = "";
                    this.batchSendFixMsgProgressData.logList = [];
                    this.batchSendFixMsgProgressData.canClose = false;

                    // 获取要处理的列表
                    var batchFixMsgList = this.fixMsgList.filter(function(fixMsg) {
                        return fixMsg.checked;
                    });

                    // 统计结果
                    var finalResult = {
                        totalNum: batchFixMsgList.length,
                        successNum: 0,
                        failNum: 0,
                        notNum: batchFixMsgList.length
                    };

                    // 显示处理进度框
                    $(".batch-send-fix-msg-progress").modal("show");
                    // 日志
                    this.batchSendFixMsgProgressPushLog("------------ 开始处理 ------------");
                    // 处理函数
                    this.batchSendFixMsgSingleProcess(batchFixMsgList, 0, finalResult);
                },
                batchSendFixMsgSingleProcess: function(batchFixMsgList, processIndex, finalResult) {
                    var that = this;

                    // 前置判断
                    if (processIndex > batchFixMsgList.length - 1) {
                        that.batchSendFixMsgProgressPushLog("------------ 处理完成 ------------");
                        that.batchSendFixMsgProgressStatisticLog(finalResult);
                        that.batchSendFixMsgProgressData.canClose = true;
                        return;
                    }

                    // 延时处理: 减轻RabbitMQ压力
                    setTimeout(function() {
                        that.batchSendFixMsgSingleSleepProcess(batchFixMsgList[processIndex], processIndex, finalResult, batchFixMsgList);
                    }, 180);
                },
                batchSendFixMsgSingleSleepProcess: function(fixMsg, processIndex, finalResult, batchFixMsgList) {
                    // 请求后台(是同步方法)
                    var processResult = this.batchSendFixMsgSingleSubmit(fixMsg);

                    // 进度条
                    processIndex++;
                    this.batchSendFixMsgProgressData.finishRatio = processIndex*1.0 / finalResult.totalNum * 100 + "%";
                    this.batchSendFixMsgProgressData.numRatioText = processIndex + " / " + finalResult.totalNum;
                    // 日志
                    this.batchSendFixMsgProgressPushLog("修复消息ID: " + processResult.fixMsgId +
                                                        " ----- 处理结果: " + (processResult.code === "SUCCESS" ? "成功" : "失败") +
                                                        " ----- 备注: " + processResult.message + " .");

                    // 统计
                    if(processResult.code === "SUCCESS") {
                        finalResult.successNum += 1;
                    } else {
                        finalResult.failNum += 1;
                    }
                    finalResult.notNum -= 1;

                    // 异常中断
                    if (processResult.code === "FAIL" && this.batchSendFixMsgData.errorContinue === "0") {
                        if (processIndex >= batchFixMsgList.length) {
                            this.batchSendFixMsgProgressPushLog("------------ 处理完成 ------------");
                            this.batchSendFixMsgProgressStatisticLog(finalResult);
                        } else {
                            this.batchSendFixMsgProgressPushLog("------------ 处理中断 ------------");
                            this.batchSendFixMsgProgressStatisticLog(finalResult);
                        }
                        this.batchSendFixMsgProgressData.canClose = true;
                        return;
                    }

                    // 处理下一条
                    this.batchSendFixMsgSingleProcess(batchFixMsgList, processIndex, finalResult);
                },
                batchSendFixMsgProgressStatisticLog: function(finalResult) {
                    this.batchSendFixMsgProgressPushLog("待处理总数: " + finalResult.totalNum +
                                                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;成功: " + finalResult.successNum +
                                                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;失败: " + finalResult.failNum +
                                                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;未处理: " + finalResult.notNum);
                },
                batchSendFixMsgProgressPushLog: function(log) {
                    this.batchSendFixMsgProgressData.logList.unshift(log);
                },
                batchSendFixMsgSingleSubmit: function(fixMsg) {
                    var that = this;

                    var processResult = {fixMsgId: fixMsg.id};
                    $.ajax(
                        {
                            url: constObj.url.sendFixMsg,
                            method: "post",
                            data: {
                                serverType: that.batchSendFixMsgData.serverType,
                                vhost: that.batchSendFixMsgData.vhost,
                                queueName: that.batchSendFixMsgData.queueName,
                                fixMsgId: fixMsg.id,
                                batched: 1
                            },
                            async: false,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    processResult.code = "FAIL";
                                    processResult.message = data.message;
                                } else {
                                    processResult.code = "SUCCESS";
                                    processResult.message = "处理成功";
                                }
                            },
                            error: function () {
                                processResult.code = "FAIL";
                                processResult.message = "请求错误";
                            }
                        }
                    );
                    return processResult;
                },
                batchSendFixMsgProgressHandle: function() {
                    this.getFixMsgList(this.query);
                    this.globalChecked = false;
                    $(".batch-send-fix-msg-progress").modal("hide");
                },
                analyseTextContent: function(textContent) {
                    var that = this;

                    that.addFixMsgAssist.msgTextList = [];
                    that.addFixMsgData.msgTextListStr = "";

                    if (!textContent) {
                        return;
                    }
                    var textItemArray = textContent.split("##");
                    textItemArray.forEach(function(textItem) {
                        textItem = textItem.trim();
                        if (!textItem) {
                            return;
                        }
                        that.addFixMsgAssist.msgTextList.push(textItem);
                        if (that.addFixMsgData.msgTextListStr) {
                            that.addFixMsgData.msgTextListStr += "##";
                        }
                        that.addFixMsgData.msgTextListStr += textItem;
                    });
                },
                msgTextTruncate: function() {
                    var that = this;

                    var msgTextPCollection = document.getElementsByClassName("msg-text-p");

                    var msgTextPArray = Array.from(msgTextPCollection);
                    if (msgTextPArray.length === 0) {
                        return;
                    }
                    msgTextPArray.forEach(function(msgTextP) {
                        var inputP = msgTextP.previousElementSibling;
                        var originChecked = inputP.checked;
                        inputP.checked = false;
                        if (msgTextP.scrollHeight > msgTextP.offsetHeight) {
                            msgTextP.classList.add("truncate");
                            inputP.checked = originChecked;
                        } else {
                            msgTextP.classList.remove("truncate");
                        }
                    });
                }
            },
            computed: {
                analyseNum: function() {
                    return this.addFixMsgAssist.msgTextList.length;
                }
            },
            created: function() {
                this.setFixId();
                this.getFix();
                this.getFixMsgList(this.query);
            },
            updated: function() {
                var that = this;

                that.$nextTick(function() {
                    this.msgTextTruncate();
                });
            }
        }
    );
</script>
</body>
</html>
