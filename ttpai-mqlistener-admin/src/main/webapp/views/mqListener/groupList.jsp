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
        .group-list .form-inline {
            margin: 20px auto;
            padding-right: 30px;
        }
        .group-list .form-inline button {
            float: right;
        }
    </style>
</head>

<body>
<div class="container-fluid" id="app">
    <div>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation"><a href="./" aria-controls="home" role="tab">服务列表</a></li>
            <li role="presentation" class="active"><a href=".group-list" aria-controls="profile" role="tab" data-toggle="tab">组列表</a></li>
            <li role="presentation"><a href="./fixList" aria-controls="profile" role="tab">修复单列表</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active group-list">
                <form class="form-inline">
                    <button type="button" @click="editGroup(false, -1)" class="btn btn-success">添加</button>
                </form>
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th width="10%">#</th>
                            <th width="20%">组名</th>
                            <th width="60%">组描述</th>
                            <th width="10%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(group, index) in groupList" :key="group.id">
                            <td v-text="index + 1"></td>
                            <td v-text="group.groupName"></td>
                            <td v-text="group.groupDesc">描述</td>
                            <td>
                                <a href="javascript:void(0)" @click="editGroup(true, group.id)" class="btn btn-primary">编辑</a>
                                <a href="javascript:void(0)" @click="deleteGroup(group.id)" class="btn btn-primary">删除</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- 添加组 -->
    <div class="modal fade edit-group" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" v-text="editTitle"></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>组名: </label>
                            <input type="text" v-model="groupNameEdit" class="form-control" placeholder="请输入组名称">
                        </div>
                        <div class="form-group">
                            <label>组描述: </label>
                            <textarea v-model="groupDescEdit" class="form-control" rows="5" placeholder="请输入组描述"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="editGroupSubmit" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 删除组 -->
    <div class="modal fade delete-group" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">删除服务</h4>
                </div>
                <div class="modal-body">
                    <p>您确认要删除吗？</p>
                    <p style="color:#EFAD4D;">此操作会删除服务的所属组</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="deleteGroupSubmit" class="btn btn-primary">提交</button>
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
            groupList: [],
            groupNameEdit: "",
            groupDescEdit: "",
            editTitle: "",
            isEdit: false,
            groupId: -1,
            deleteGroupId: -1
        },
        methods: {
            initGroupList: function() {
                this.getGroupList();
            },
            getGroupList: function() {
                var that = this;

                $.ajax({
                    url: "./groupListData",
                    method: "get",
                    dataType: "json",
                    beforeSend: function (XMLHttpRequest) {
                       XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                    },
                    success: function (data) {
                       if (data.code !== "200") {
                           alert(data.message);
                       } else {
                           that.groupList = data.result;
                       }
                    }
                });
            },
            editGroup: function(isEdit, groupId) {
                var that = this;
                this.clearEditGroup();

                if (!isEdit) {
                    this.isEdit = false;
                    this.groupId = -1;
                    this.editTitle = "添加组";
                    $(".edit-group").modal("show");
                } else {
                    $.ajax({
                        url: "./getGroup",
                        method: "get",
                        data: {
                           groupId: groupId
                        },
                        dataType: "json",
                        beforeSend: function (XMLHttpRequest) {
                           XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                        },
                        success: function (data) {
                            if (data.code !== "200") {
                                alert(data.message);
                            } else {
                                var groupInfo = data.result;

                                that.editTitle = "编辑组";
                                that.isEdit = true;
                                that.groupId = groupId;
                                that.groupNameEdit = groupInfo.groupName;
                                that.groupDescEdit = groupInfo.groupDesc;
                                $(".edit-group").modal("show");
                            }
                        }
                    });
                }
            },
            editGroupSubmit: function () {
                var that = this;

                var groupName = this.groupNameEdit;
                var groupDesc = this.groupDescEdit;
                var groupId = this.isEdit ? this.groupId : "";

                $.ajax({
                    url: "./editGroup",
                    method: "post",
                    data: {
                        id: groupId,
                        groupName: groupName,
                        groupDesc: groupDesc
                    },
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function (data) {
                        if (data.code !== "200") {
                            alert(data.message);
                        } else {
                            $(".edit-group").modal("hide");
                            that.getGroupList();
                            that.clearEditGroup();
                        }
                    }
                });
            },
            clearEditGroup: function() {
                this.groupNameEdit = "";
                this.groupDescEdit = "";
                this.editTitle = "";
                this.isEdit = false;
                this.groupId = -1;
            },
            deleteGroup: function(groupId) {
                this.deleteGroupId = groupId;
                $(".delete-group").modal("show");
            },
            deleteGroupSubmit: function() {
                var that = this;
                $.ajax({
                    url: "./deleteGroup",
                    method: "get",
                    data: {
                       groupId: that.deleteGroupId
                    },
                    dataType: "json",
                    beforeSend: function(XMLHttpRequest) {
                        XMLHttpRequest.setRequestHeader("X-Requested-With","XMLHttpRequest");
                    },
                    success: function (data) {
                        if (data.code !== "200") {
                            alert(data.message);
                        } else {
                            that.deleteGroupId = -1;
                            $(".delete-group").modal("hide");
                            that.getGroupList();
                        }
                    }
                });
            }
        },
        mounted: function() {
            this.initGroupList();
        }
    });
</script>
</body>
</html>
