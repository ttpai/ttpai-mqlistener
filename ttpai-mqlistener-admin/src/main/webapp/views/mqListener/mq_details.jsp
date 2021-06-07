<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="./js/jquery.min.js"></script>
<script src="./js/bootstrap.min.js"></script>
<link href="./css/bootstrap.min.css" rel="stylesheet"/>
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
    <script type="text/javascript">
        var reportError = window.reportError || function () {};
    </script>
</head>

<body>

<!--右边的具体内容-->
<div class="content-panel">
    <!--顶部的路径导航以及工具条-->
    <div class="location-toolbar clearfix">
        <a href="./"  class="btn btn-primary assign-batch">回到项目列表</a>
    </div>

    <!-- 标签切换 -->
    <ul id="myTabs" class="nav nav-tabs mt10 ml10">
        <li class="active"><a href="#">${path}监听类列表</a></li>
    </ul>

    <!--过滤表单-->
    <form class="filter-form form-inline form-group-sm" method="post" action="" id="allocationForm">

        <div class="table-container">
            <table class="table table-condensed table-striped">
                <thead>
                <tr>
                    <th width="50px" class="text-center">#</th>
                    <th width="200px">类别</th>
                    <th>信息</th>
                    <th width="100px">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="text-center arial">1</td>
                    <td class="text-center arial">监听器配置</td>
                    <td>
                        <c:choose>
                            <c:when test="${null ne listenerConfig}">
                                <c:if test="${listenerConfig.processType eq 1}">
                                    <p>处理方式：直接放入异常队列</p>
                                    <p>异常队列名称：${listenerConfig.exceptionQueue}</p>
                                </c:if>
                                <c:if test="${listenerConfig.processType eq 2}">
                                    <p>处理方式：放回队头或队尾</p>
                                    <p>队头/队尾：
                                        <c:choose>
                                            <c:when test="${null ne listenerConfig.isReturnHead && listenerConfig.isReturnHead eq true}">
                                                队头
                                            </c:when>
                                            <c:otherwise>
                                                队尾
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </c:if>
                                <c:if test="${listenerConfig.processType eq 3}">
                                    <p>处理方式：放回队尾，消费一定次数后丢弃</p>
                                    <p>消费次数：${listenerConfig.failLimit}次</p>
                                </c:if>
                                <c:if test="${listenerConfig.processType eq 4}">
                                    <p>处理方式：放回队尾，消费一定次数后放入异常队列</p>
                                    <p>消费次数：${listenerConfig.failLimit}次</p>
                                    <p>异常队列名称：${listenerConfig.exceptionQueue}</p>
                                </c:if>
                                <p>并发消费数：${listenerConfig.concurrentConsumers}</p>
                                <p>预抓取消息数：${listenerConfig.prefetchCount ne null ? listenerConfig.prefetchCount : 1}</p>
                                <p>消费间隔毫秒数：${listenerConfig.consumeIntervalMs ne null ? listenerConfig.consumeIntervalMs : 0}</p>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <!-- 按钮触发模态框 -->
                        <button type="button" class="btn btn-primary btn-lg" data-toggle="modal"
                                data-target="#myModal">
                            修改配置
                        </button>
                    </td>
                </tr>

                <tr>
                    <td class="text-center arial">2</td>
                    <td class="text-center arial">监听器描述</td>
                    <td>${mqDesc}</td>
                    <td>
                        <!-- 按钮触发模态框 -->
                        <button type="button" class="btn btn-primary btn-lg" data-toggle="modal"
                                data-target="#myDesc">
                            修改描述
                        </button>
                    </td>
                </tr>

                <tr>
                    <td class="text-center arial">3</td>
                    <td class="text-center arial">管理员</td>
                    <td>${manager}</td>
                    <td>
                        <!-- 按钮触发模态框 -->
                        <button type="button" class="btn btn-primary btn-lg" data-toggle="modal"
                                data-target="#myManager">
                            修改管理员
                        </button>
                    </td>
                </tr>

                <tr>
                    <td class="text-center arial">4</td>
                    <td class="text-center arial">队列</td>
                    <td>${queue}</td>
                    <td></td>
                </tr>

                <tr>
                    <td class="text-center arial">5</td>
                    <td class="text-center arial">监听类实例节点集合</td>
                    <td></td>
                    <td>
                        <a href="./getNodeList?path=${path}" class="btn btn-primary assign-batch">查看详情</a>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <!--最下面的分页器-->
    </form>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改MQ配置</h4>
            </div>
            <div class="modal-body">
                <c:choose>
                    <c:when test="${listenerConfig eq null}">
                        <c:set var="isInit" value="1" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="isInit" value="0" scope="request"/>
                    </c:otherwise>
                </c:choose>
                <div class="form-group">
                    <label><i style="color: red;">* </i>处理方式</label>
                    <select class="form-control" id="processType" onchange="processTypeChange()">
                        <option value="" <c:if test="${isInit eq 1}">selected</c:if>>请选择</option>
                        <option value="1" <c:if test="${isInit eq 0 && listenerConfig.processType eq 1}">selected</c:if>>直接放入异常队列</option>
                        <option value="2" <c:if test="${isInit eq 0 && listenerConfig.processType eq 2}">selected</c:if>>放回队头或队尾</option>
                        <option value="3" <c:if test="${isInit eq 0 && listenerConfig.processType eq 3}">selected</c:if>>放回队尾，消费一定次数后丢弃</option>
                        <option value="4" <c:if test="${isInit eq 0 && listenerConfig.processType eq 4}">selected</c:if>>放回队尾，消费一定次数后放入异常队列</option>
                    </select>
                </div>
                <c:choose>
                    <c:when test="${!(isInit eq 0 && listenerConfig.processType eq 2)}">
                        <c:set var="isReturnHeadHide" value="1" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="isReturnHeadHide" value="0" scope="request"/>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${!(isInit eq 0 && (listenerConfig.processType eq 3 || listenerConfig.processType eq 4))}">
                        <c:set var="failLimitHide" value="1" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="failLimitHide" value="0" scope="request"/>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${!(isInit eq 0 && (listenerConfig.processType eq 1 || listenerConfig.processType eq 4))}">
                        <c:set var="exceptionQueueHide" value="1" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="exceptionQueueHide" value="0" scope="request"/>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${isInit eq 1 || listenerConfig.isReturnHead eq null}">
                        <c:set var="headSelected" value="0" scope="request"/>
                    </c:when>
                    <c:when test="${listenerConfig.isReturnHead eq true}">
                        <c:set var="headSelected" value="1" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="headSelected" value="2" scope="request"/>
                    </c:otherwise>
                </c:choose>
                <div class="form-group <c:if test="${isReturnHeadHide eq 1}">hide</c:if>"  data-item="isReturnHead">
                    <label><i style="color: red;">* </i>队头/队尾</label>
                    <select class="form-control" id="isReturnHead">
                        <option value="" <c:if test="${headSelected eq 0}">selected</c:if>>请选择</option>
                        <option value="true" <c:if test="${headSelected eq 1}">selected</c:if>>队头</option>
                        <option value="false" <c:if test="${headSelected eq 2}">selected</c:if>>队尾</option>
                    </select>
                </div>
                <div class="form-group <c:if test="${failLimitHide eq 1}">hide</c:if>" data-item="failLimit">
                    <label><i style="color: red;">* </i>消费失败次数</label>
                    <input id="failLimit" class="form-control" value="<c:if test="${isInit eq 0}">${listenerConfig.failLimit}</c:if>"/>
                </div>
                <div class="form-group <c:if test="${exceptionQueueHide eq 1}">hide</c:if>" data-item="exceptionQueue">
                    <label><i style="color: red;">* </i>异常队列名称</label>
                    <input id="exceptionQueue" class="form-control" value="<c:if test="${isInit eq 0}">${listenerConfig.exceptionQueue}</c:if>"/>
                </div>
                <div class="form-group" data-item="concurrentConsumers">
                    <label><i style="color: red;">* </i>并发消费数</label>
                    <input id="concurrentConsumers" class="form-control"  value="<c:if test="${isInit eq 0}">${listenerConfig.concurrentConsumers}</c:if>"/>
                </div>
                <div class="form-group" data-item="prefetchCount">
                    <label><i style="color: red;">* </i>预抓取消息数</label>
                    <input id="prefetchCount" class="form-control"  value="<c:if test="${isInit eq 0}">${listenerConfig.prefetchCount ne null ? listenerConfig.prefetchCount : 1}</c:if>"/>
                </div>
                <div class="form-group" data-item="consumeIntervalMs">
                    <label><i style="color: red;">* </i>消费间隔毫秒数(<i style="color: #EFAD4D;">为0时代表消费无间隔; 大于0时代表消费有间隔</i>)</label>
                    <input id="consumeIntervalMs" class="form-control"  value="<c:if test="${isInit eq 0}">${listenerConfig.consumeIntervalMs ne null ? listenerConfig.consumeIntervalMs : 0}</c:if>"/>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" onclick="saveConfig()" class="btn btn-primary">提交</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myDesc" tabindex="-1" role="dialog" aria-labelledby="myDescLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myDescLabel">修改MQ描述</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>监听类描述</label>
                    <textarea id="desc" class="form-control" rows="3">${mqDesc}</textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" onclick="savedesc()" class="btn btn-primary">提交</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myManager" tabindex="-1" role="dialog" aria-labelledby="myDescLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myManagerLabel">修改管理员</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>管理员</label>
                    <textarea id="manager" class="form-control" rows="3">${manager}</textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" onclick="savemanager()" class="btn btn-primary">提交</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function refresh() {
        window.location.reload();
    }

    function saveConfig() {

        var processType = $("#processType").val();
        var isReturnHead = $("#isReturnHead").val();
        var failLimit = $("#failLimit").val();
        var exceptionQueue = $("#exceptionQueue").val();
        var concurrentConsumers = $("#concurrentConsumers").val();
        var prefetchCount = $("#prefetchCount").val();
        var consumeIntervalMs = $("#consumeIntervalMs").val();

        var path = "${path}";

        $.ajax({
                   type: 'post',
                   dataType: 'json',
                   url: './amendConfig',
                   data: {
                       path: path,
                       processType: processType,
                       isReturnHead: isReturnHead,
                       failLimit: failLimit,
                       exceptionQueue: exceptionQueue,
                       concurrentConsumers: concurrentConsumers,
                       prefetchCount: prefetchCount,
                       consumeIntervalMs: consumeIntervalMs
                   },
                   beforeSend : function (XMLHttpRequest) {
                       XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                   },
                   success: function (data) {
                       $('#myModal').modal('hide');
                       alert(data.message);
                       refresh();
                   },
                   error: function (data) {
                       $('#myModal').modal('hide');
                       alert("系统异常");
                   },
               });
    }

    function savedesc() {

        var desc = document.getElementById("desc").value;
        var path = "${path}";

        $.ajax({
                   type: 'post',
                   dataType: 'json',
                   url: './amendDesc',
                   data: {
                       path: path,
                       desc: desc
                   },
                   beforeSend : function (XMLHttpRequest) {
                       XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                   },
                   success: function (data) {
                       $('#myDesc').modal('hide');
                       alert(data.message);
                       refresh();
                   },
                   error: function (data) {
                       $('#myDesc').modal('hide');
                       alert("系统异常");
                   },
               });
    }

    function savemanager() {

        var manager = document.getElementById("manager").value;
        var path = "${path}";

        $.ajax({
                   type: 'post',
                   dataType: 'json',
                   url: './amendManager',
                   data: {
                       path: path,
                       manager: manager
                   },
                   beforeSend : function (XMLHttpRequest) {
                       XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                   },
                   success: function (data) {
                       $('#myManager').modal('hide');
                       alert(data.message);
                       refresh();
                   },
                   error: function (data) {
                       $('#myManager').modal('hide');
                       alert("系统异常");
                   },
               });
    }

    function processTypeChange() {
        var processType = $("#processType").val();

        switch (processType) {
            case "1":
                show4Process1();
                break;
            case "2":
                show4Process2();
                break;
            case "3":
                show4Process3();
                break;
            case "4":
                show4Process4();
                break;
            default:
                show4Null();
        }
    }

    function show4Process1() {
        $("div[data-item='isReturnHead']").addClass("hide");
        $("div[data-item='failLimit']").addClass("hide");
        $("div[data-item='exceptionQueue']").removeClass("hide");
    }

    function show4Process2() {
        $("div[data-item='isReturnHead']").removeClass("hide");
        $("div[data-item='failLimit']").addClass("hide");
        $("div[data-item='exceptionQueue']").addClass("hide");
    }

    function show4Process3() {
        $("div[data-item='isReturnHead']").addClass("hide");
        $("div[data-item='failLimit']").removeClass("hide");
        $("div[data-item='exceptionQueue']").addClass("hide");
    }

    function show4Process4() {
        $("div[data-item='isReturnHead']").addClass("hide");
        $("div[data-item='failLimit']").removeClass("hide");
        $("div[data-item='exceptionQueue']").removeClass("hide");
    }

    function show4Null() {
        $("div[data-item='isReturnHead']").addClass("hide");
        $("div[data-item='failLimit']").addClass("hide");
        $("div[data-item='exceptionQueue']").addClass("hide");
    }
</script>
</body>
</html>
