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
        .service-list .form-control {
            width: auto;
        }
        .service-list .form-inline {
            margin: 20px auto;
        }
        .service-list .form-group:nth-child(1) {
            padding-left: 10px;
        }
        .service-list .form-group {
            padding-right: 30px;
        }
        .service-page {
            overflow: hidden;
        }
        .service-page .pagination {
            margin: 0 auto;
            padding-right: 15px;
        }
        .edit-service textarea {
            resize: vertical;
        }
    </style>
</head>

<body>
<div class="container-fluid" id="app">
    <div>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href=".service-list" aria-controls="home" role="tab" data-toggle="tab">服务列表</a></li>
            <li role="presentation"><a href="./groupList" aria-controls="profile" role="tab">组列表</a></li>
            <li role="presentation"><a href="./fixList" aria-controls="profile" role="tab">修复单列表</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active service-list">
                <form class="form-inline">
                    <div class="form-group">
                        <label>服务名称: </label>
                        <input type="text" class="form-control" v-model="serviceNameQuery" placeholder="请输入服务名称">
                    </div>
                    <div class="form-group">
                        <label>所属组: </label>
                        <select class="form-control group-query" v-model="groupQuery">
                            <option value="-1">请选择所属组</option>
                            <option v-for="group in groupList" :key="group.id" :value="group.id" v-text="group.groupName"></option>
                        </select>
                    </div>
                    <button type="button" @click="serviceQuery" class="btn btn-primary">查询</button>
                    <button type="button" @click="clearQuery" class="btn btn-primary">清空条件</button>
                    <button type="button" @click="editService(false, -1)" class="btn btn-success">添加服务</button>
                </form>
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th width="10%">#</th>
                            <th width="20%">服务名称</th>
                            <th width="35%">服务描述</th>
                            <th width="20%">所属组</th>
                            <th width="15%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(service, index) in serviceList" :key="service.id">
                            <td v-text="sequenceNum(index)"></td>
                            <td v-text="service.serviceName"></td>
                            <td v-text="service.serviceDesc"></td>
                            <td v-text="service.groupName"></td>
                            <td>
                                <a :href="'./getRealList?severPath=' + service.serviceName"  class="btn btn-primary">查看详情</a>
                                <a href="javascript:void(0)" @click="editService(true, service.id)" class="btn btn-primary">编辑</a>
                                <a href="javascript:void(0)" @click="deleteService(service.id)" class="btn btn-primary">删除</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <nav aria-label="Page navigation" class="service-page">
                    <ul class="pagination pull-right">
                        <li>
                            <a href="javascript:void(0)" @click="prePage" aria-label="Previous">
                                <span aria-hidden="true">上一页</span>
                            </a>
                        </li>
                        <li><a href="javascript:void(0)" v-text="nowPage"></a></li>
                        <li>
                            <a href="javascript:void(0)" @click="nextPage" aria-label="Next">
                                <span aria-hidden="true">下一页</span>
                            </a>
                        </li>
                        <li>
                            <span>共<span v-text="totalPage"></span>页</span>
                        </li>
                        <li>
                            <span>共<span v-text="totalItem"></span>条</span>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
    <!-- 添加/编辑服务 -->
    <div class="modal fade edit-service" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" v-text="editTitle"></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>服务名称: </label>
                            <input type="text" v-model="serviceNameEdit" class="form-control" placeholder="请输入服务名称">
                        </div>
                        <div class="form-group">
                            <label>服务描述: </label>
                            <textarea class="form-control" v-model="serviceDescEdit" rows="5" placeholder="请输入服务描述"></textarea>
                        </div>
                        <div class="form-group">
                            <label>所属组: </label>
                            <select class="form-control" v-model="groupEdit">
                                <option value="-1">请选择所属组</option>
                                <option v-for="group in groupList" :key="group.id" :value="group.id" v-text="group.groupName"></option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="editServiceSubmit" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 删除服务 -->
    <div class="modal fade delete-service" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">删除服务</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除吗？</p>
                    <p style="color:#EFAD4D;">请确认项目不存在！！</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="deleteServiceSubmit" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="./js/jquery.min.js"></script>
<script src="./js/bootstrap.min.js"></script>
<script src="./js/vue.min.js"></script>
<script>
    var vm = new Vue({
        el: "#app",
        data: {
            serviceNameQuery: "",
            groupQuery: "-1",
            groupList: [],
            servicePage: {},
            serviceList: [],
            nowPage: 1,
            totalPage: 1,
            totalItem: 0,
            isEdit: false,
            editTitle: "",
            serviceId: -1,
            serviceNameEdit: "",
            serviceDescEdit: "",
            groupEdit: "-1",
            deleteServiceId: -1
        },
        methods: {
            initGroupList: function() {
                var that = this;
                $.ajax({
                    url: "./groupListData",
                    method: "get",
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function(data) {
                        if (data.code !== "200") {
                           alert(data.message);
                        } else {
                           that.groupList = data.result;
                        }
                    }
                });
            },
            initServiceList: function() {
                this.getServiceList(1);
            },
            getServiceList: function(toPage) {
                var serviceName = this.serviceNameQuery;
                var groupId = this.groupQuery;
                var nowPage = toPage;

                var that = this;
                $.ajax({
                    url: "./servicePage",
                    method: "get",
                    data: {
                        serviceName: serviceName,
                        groupId: groupId,
                        nowPage: nowPage
                    },
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function(data) {
                        if (data.code !== "200") {
                            alert(data.message);
                        } else {
                            that.servicePage = data.result;
                            that.serviceList = data.result.dataList;
                            that.nowPage = that.servicePage.currentPage;
                            that.totalPage = that.servicePage.countPage;
                            that.totalItem = that.servicePage.totalNum;
                        }
                    }
                });
            },
            serviceQuery: function() {
                this.getServiceList(1);
            },
            prePage: function() {
                if (this.nowPage <= 1) {
                    return;
                }
                this.getServiceList(this.nowPage - 1);
            },
            nextPage: function() {
                if (this.nowPage >= this.totalPage) {
                    return;
                }
                this.getServiceList(this.nowPage + 1);
            },
            clearQuery: function() {
                this.serviceNameQuery = "";
                this.groupQuery = "-1";
                this.getServiceList(1);
            },
            editService: function(isEdit, serviceId) {
                var that = this;
                this.clearEditService();

                if (!isEdit) {
                    this.isEdit = false;
                    this.serviceId = -1;
                    this.editTitle = "添加服务";
                    $(".edit-service").modal("show");
                } else {
                    $.ajax({
                        url: "./getService",
                        method: "get",
                        data: {
                           serviceId: serviceId
                        },
                        dataType: "json",
                        beforeSend: function(XMLHttpRequest) {
                            XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                        },
                        success: function (data) {
                            if (data.code !== "200") {
                                alert(data.message);
                            } else {
                                var serviceInfo = data.result;

                                that.editTitle = "编辑服务";
                                that.isEdit = true;
                                that.serviceId = serviceId;
                                that.serviceNameEdit = serviceInfo.serviceName;
                                that.serviceDescEdit = serviceInfo.serviceDesc;
                                that.groupEdit = serviceInfo.groupId;
                                $(".edit-service").modal("show");
                            }
                        }
                    });
                }
            },
            editServiceSubmit: function() {
                var that = this;

                var serviceName = this.serviceNameEdit;
                var serviceDesc = this.serviceDescEdit;
                var groupId = this.groupEdit;
                var serviceId = this.isEdit ? this.serviceId : "";

                $.ajax({
                    url: "./editService",
                    method: "post",
                    data: {
                       id: serviceId,
                       serviceName: serviceName,
                       serviceDesc: serviceDesc,
                       groupId: groupId
                    },
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function (data) {
                        if (data.code !== "200") {
                            alert(data.message);
                        } else {
                            $(".edit-service").modal("hide");
                            that.getServiceList(that.nowPage);
                            that.clearEditService();
                        }
                    }
                });
            },
            clearEditService: function() {
                this.editTitle = "";
                this.serviceNameEdit = "";
                this.serviceDescEdit = "";
                this.groupEdit = -1;
                this.serviceId = -1;
            },
            deleteService: function(serviceId) {
                this.deleteServiceId = serviceId;
                $(".delete-service").modal("show");
            },
            deleteServiceSubmit: function() {
                var that = this;
                $.ajax({
                    url: "./deleteService",
                    method: "get",
                    data: {
                        serviceId: that.deleteServiceId
                    },
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function (data) {
                        if (data.code !== "200") {
                            alert(data.message);
                        } else {
                            that.deleteServiceId = -1;
                            $(".delete-service").modal("hide");
                            that.getServiceList(that.nowPage);
                        }
                    }
                });
            },
            sequenceNum: function(index) {
                return (index + 1) + (this.nowPage - 1)*10;
            }
        },
        mounted: function() {
            this.initGroupList();
            this.initServiceList();
        }
    });
</script>
</body>
</html>
