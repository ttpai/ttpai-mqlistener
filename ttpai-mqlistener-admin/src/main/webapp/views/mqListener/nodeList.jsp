<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
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
        var reportError = window.reportError || function(){};
    </script>
    <style type="text/css">
        prev {
            white-space: pre;
            display: none;
            position: absolute;
            background-color: #FFF5EE;
            border: 1px solid #B5B5B5;
            padding: 8px;
            overflow-x: auto;
            max-width: 800px;
        }
        #errorInfoBox:checked ~ prev {
            display: block;
        }

        #errorMsgBox:checked ~ prev {
            display: block;
        }
    </style>
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
        <li class="active"><a href="#">${path}实例节点集合列表</a></li>
    </ul>

    <!--过滤表单-->
    <form class="filter-form form-inline form-group-sm" method="post" action="" id="allocationForm">

        <div class="table-container">
            <table class="table table-condensed table-striped">
                <thead>
                <tr>
                    <th width="20" class="text-center">#</th>
                    <th width="150px">节点名</th>
                    <th width="100px">是否启用</th>
                    <th width="100px">是否在线</th>
                    <th width="200px">错误数据</th>
                    <th>错误信息</th>
                    <th width="280px">错误产生时间</th>
                    <th width="100px">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="node" items="${nodeVOList}" varStatus="status">
                    <tr>
                        <td class="text-center arial">${status.index +1}</td>
                        <td>${node.path}</td>
                        <td>
                            <c:choose>
                                <c:when test="${node.inUse eq 1 }">
                                    启用
                                </c:when>
                                <c:otherwise>
                                    停用
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${node.online eq 1 }">
                                    在线
                                </c:when>
                                <c:otherwise>
                                    下线
                                </c:otherwise>
                            </c:choose>
                        </td>
                        </td>
                        <c:choose>
                            <c:when test="${empty node.errorInfo}">
                                <td></td>
                            </c:when>
                            <c:otherwise>
                                <td style="color: #EFAD4D; position: relative;">
                                    出错了！<label for="errorInfoBox" style="text-decoration: underline; cursor: pointer;">查看此条消息</label><input id="errorInfoBox" type="checkbox" style="width: 0; height: 0;"/>
                                    <prev>${node.errorInfo}</prev>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${empty node.errorMsg}">
                                <td></td>
                            </c:when>
                            <c:otherwise>
                                <td style="color: #EFAD4D;">
                                    出错了！<label for="errorMsgBox"style="text-decoration: underline; cursor: pointer;">查看异常信息</label><input id="errorMsgBox" type="checkbox" style="width: 0; height: 0;"/>
                                    <prev>${node.errorMsg}</prev>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td style="color: #EFAD4D;">${node.errorTime}</td>
                        <td>
                            <c:choose>
                                <c:when test="${node.inUse eq 1 }">
                                    <a href="./stopNode?path=${path}&nodePath=${node.path}"  class="btn btn-primary assign-batch">停用</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="./startNode?path=${path}&nodePath=${node.path}"  class="btn btn-primary assign-batch">启用</a>
                                </c:otherwise>
                            </c:choose>
                            <a onClick="takeOut('./deleteInstance?path=${path}&nodePath=${node.path}')" class="btn btn-primary assign-batch">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!--最下面的分页器-->
    </form>
</div>
<!-- 信息删除确认 -->
<div class="modal fade" id="delcfmModel">
    <div class="modal-dialog">
        <div class="modal-content message_align">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title">提示信息</h4>
            </div>
            <div class="modal-body">
                <p>您确认要删除吗？</p>
                <p style="color:#EFAD4D;">请确认实例不存在！！</p>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="url"/>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <a  onclick="urlSubmit()" class="btn btn-success" data-dismiss="modal">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
    function takeOut(url) {
        $('#url').val(url);//给会话中的隐藏属性URL赋值
        $('#delcfmModel').modal();
    }
    function urlSubmit(){
        var url=$.trim($("#url").val());//获取会话中的隐藏属性URL
        window.location.href=url;
    }
</script>
</body>
</html>
