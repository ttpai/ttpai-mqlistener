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
        .fix-guide {
            margin: 15px auto;
        }
        .fix-guide .fix-guide-arrow {
            font-size: 25px;
        }
        .fix-guide .fix-guide-title {
            font-size: 18px;
            font-weight: bold;
        }
        .fix-guide .fix-guide-text {
            font-size: 18px;
            margin-left: 30px;
            margin-right: 30px;
        }
        .fix-guide span:nth-child(2) {
            margin-left: 10px;
        }
        .fix-list .form-control {
            width: auto;
        }
        .fix-list .form-inline {
            margin: 20px auto;
        }
        .fix-list .form-group:nth-child(1) {
            padding-left: 10px;
        }
        .fix-list .form-group {
            padding-right: 30px;
        }
        .fix-page {
            overflow: hidden;
        }
        .fix-page .pagination {
            margin: 0 auto;
            padding-right: 15px;
        }
    </style>
</head>

<body>
<div class="container-fluid" id="app">
    <div>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation"><a href="./" aria-controls="home" role="tab">服务列表</a></li>
            <li role="presentation"><a href="./groupList" aria-controls="profile" role="tab">组列表</a></li>
            <li role="presentation" class="active"><a href=".fix-list" aria-controls="profile" role="tab" data-toggle="tab">修复单列表</a></li>
        </ul>
        <div class="alert alert-info fix-guide" role="alert">
            <span class="fix-guide-title">修复流程：</span>
            <span class="fix-guide-text">添加修复单</span>
            <span class="fix-guide-arrow">》</span>
            <span class="fix-guide-text">查看修复单</span>
            <span class="fix-guide-arrow">》</span>
            <span class="fix-guide-text">添加消息</span>
            <span class="fix-guide-arrow">》</span>
            <span class="fix-guide-text">修复消息</span>
            <span class="fix-guide-arrow">》</span>
            <span class="fix-guide-text">结束修复单</span>
        </div>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active fix-list">
                <form class="form-inline">
                    <div class="form-group">
                        <label>修复状态: </label>
                        <select class="form-control" v-model="query.fixStatus">
                            <option value="">请选择状态</option>
                            <option value="0">修复中</option>
                            <option value="1">已结束</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>负责人: </label>
                        <input type="text" class="form-control" v-model="query.chargePerson" placeholder="请输入负责人">
                    </div>
                    <button type="button" @click="queryFixHandle" class="btn btn-primary">查询</button>
                    <button type="button" @click="queryClearHandle" class="btn btn-primary">清空条件</button>
                    <button type="button" @click="addFixHandle" class="btn btn-success">添加</button>
                </form>
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th width="10%">#</th>
                        <th width="20%">名称</th>
                        <th width="10%">修复状态</th>
                        <th width="20%">备注</th>
                        <th width="15%">创建时间</th>
                        <th width="10%">负责人</th>
                        <th width="15%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(fix, index) in fixList" :key="fix.id">
                        <td v-text="index + 1"></td>
                        <td v-text="fix.fixName"></td>
                        <td v-text="$options.filters.fixStatusText(fix.fixStatus)"></td>
                        <td v-text="fix.fixRemark"></td>
                        <td v-text="$options.filters.date(fix.createTime)"></td>
                        <td v-text="fix.chargePerson"></td>
                        <td>
                            <a :href="'./fixMsgList?fixId=' + fix.id" class="btn btn-primary">查看</a>
                            <a href="javascript:void(0)" v-if="fix.fixStatus === 0" @click="editFixHandle(fix.id)" class="btn btn-primary">编辑</a>
                            <a href="javascript:void(0)"  v-if="fix.fixStatus === 0" @click="endFixHandle(fix.id)" class="btn btn-primary">结束修复</a>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" class="fix-page">
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
    <!-- 添加/编辑 修复数据 -->
    <div class="modal fade add-or-edit" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="addOrEditAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" v-text="addOrEditAssist.title"></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label>修复名称: </label>
                            <input type="text" v-model="addOrEditData.fixName" class="form-control" placeholder="请输入修复名称">
                        </div>
                        <div class="form-group">
                            <label>负责人: </label>
                            <input type="text" v-model="addOrEditData.chargePerson" class="form-control" placeholder="请输入负责人">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="addOrEditAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="addOrEditFixSubmitHandle" :disabled="addOrEditAssist.isInProcess" class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 结束修复 -->
    <div class="modal fade prompt-end" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" :disabled="endFixAssist.isInProcess" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">结束修复</h4>
                </div>
                <div class="modal-body">
                    <section v-if="endFixAssist.hasNeedFix === 0">
                        <div class="alert alert-warning" role="alert">
                            <h4>该修复下还没有消息！</h4>
                        </div>
                        <p>您确认要结束吗？</p>
                        <p style="color:#EFAD4D;">此操作不可恢复！</p>
                    </section>
                    <section v-else-if="endFixAssist.hasNeedFix === 1 && endFixAssist.allFixSuccess === 1">
                        <div class="alert alert-info" role="alert">
                            <h4>该修复下消息都处理成功！</h4>
                            <p>消息总数: {{endFixAssist.needFixNum}}</p>
                        </div>
                        <p>您确认要结束吗？</p>
                        <p style="color:#EFAD4D;">此操作不可恢复！</p>
                    </section>
                    <section v-else>
                        <div class="alert alert-danger" role="alert">
                            <h4>该修复下有未修复成功的消息！</h4>
                            <p>消息总数: {{endFixAssist.needFixNum}}, 待修复: {{endFixAssist.waitFixNum}}, 修复中: {{endFixAssist.inFixNum}}, 成功: {{endFixAssist.fixSuccessNum}}, 异常: {{endFixAssist.fixErrorNum}}</p>
                        </div>
                        <p>您确认要结束吗？</p>
                        <p style="color:#EFAD4D;">此操作不可恢复！</p>
                    </section>
                    <form>
                        <hr/>
                        <div class="form-group">
                            <label>备注: </label>
                            <input type="text" v-model="endFixData.remark" class="form-control" placeholder="请输入备注">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" :disabled="endFixAssist.isInProcess" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" @click="endFixSubmitHandle" :disabled="endFixAssist.isInProcess" class="btn btn-primary">提交</button>
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
            fixListData: "./fixListData",
            addOrEditFix: "./addOrEditFix",
            getFix4Edit: "./getFix4Edit",
            fixSummary: "./fixSummary",
            endFix: "./endFix"
        },
        page: {
            defaultPageSize: 10
        },
        addOrEdit: {
            addTitle: "添加修复",
            editTitle: "修改修复"
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
                query: {
                    chargePerson: "",
                    fixStatus: "",
                    pageNum: 1,
                    pageSize: constObj.page.defaultPageSize
                },
                fixList: [],
                pageBar: {
                    noPrePage: true,
                    noNextPage: true,
                    totalPage: 0,
                    totalItem: 0,
                },
                addOrEditAssist: {
                    title: "",
                    isInProcess: false
                },
                addOrEditData: {
                    id: "",
                    fixName: "",
                    chargePerson: ""
                },
                endFixAssist: {
                    isInProcess: false,
                    needFixNum: "",
                    waitFixNum: "",
                    inFixNum: "",
                    fixSuccessNum: "",
                    fixErrorNum: "",
                    hasNeedFix: "",
                    allFixSuccess: ""
                },
                endFixData: {
                    fixId: "",
                    remark: ""
                }
            },
            filters: {
                fixStatusText: function(fixStatus) {
                    return constObj.fixStatus[fixStatus];
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
                }
            },
            methods: {
                // 条件查询
                queryFixHandle: function() {
                    var queryClone = JSON.parse(JSON.stringify(this.query));
                    queryClone.pageNum = 1;

                    this.getFixList(queryClone);
                },
                // 清空条件查询
                queryClearHandle: function() {
                    this.query.chargePerson = "";
                    this.query.fixStatus = "";
                    this.query.pageNum = 1;
                    this.getFixList(this.query);
                },
                // 添加修复
                addFixHandle: function() {
                    this.clearAddOrEditData();
                    this.addOrEditAssist.title = constObj.addOrEdit.addTitle;
                    $(".add-or-edit").modal("show");
                },
                // 编辑修复
                editFixHandle: function(fixId) {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.getFix4Edit,
                            method: "get",
                            data: {
                                fixId: fixId
                            },
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.clearAddOrEditData();
                                    that.addOrEditAssist.title = constObj.addOrEdit.editTitle;
                                    that.addOrEditData.id = fixId;
                                    that.addOrEditData.fixName = data.result.fixName;
                                    that.addOrEditData.chargePerson = data.result.chargePerson;
                                    $(".add-or-edit").modal("show");
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                // 添加/编辑修复提交
                addOrEditFixSubmitHandle: function() {
                    var that = this;

                    that.addOrEditAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.addOrEditFix,
                            method: "post",
                            data: that.addOrEditData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.addOrEditAssist.isInProcess = false;

                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixList(that.query);
                                    $(".add-or-edit").modal("hide");
                                }
                            },
                            error: function () {
                                that.addOrEditAssist.isInProcess = false;
                                alert("请求错误");
                            }
                        }
                    );
                },
                // 结束修复
                endFixHandle: function(fixId) {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.fixSummary,
                            method: "get",
                            data: {
                                fixId: fixId
                            },
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.endFixAssist.needFixNum = data.result.needFixNum;
                                    that.endFixAssist.waitFixNum = data.result.waitFixNum;
                                    that.endFixAssist.inFixNum = data.result.inFixNum;
                                    that.endFixAssist.fixSuccessNum = data.result.fixSuccessNum;
                                    that.endFixAssist.fixErrorNum = data.result.fixErrorNum;
                                    that.endFixAssist.hasNeedFix = data.result.hasNeedFix;
                                    that.endFixAssist.allFixSuccess = data.result.allFixSuccess;

                                    that.endFixData.fixId = fixId;
                                    that.endFixData.remark = "";
                                    $(".prompt-end").modal("show");
                                }
                            },
                            error: function () {
                                alert("请求错误");
                            }
                        }
                    );
                },
                endFixSubmitHandle: function() {
                    var that = this;

                    that.endFixAssist.isInProcess = true;

                    $.ajax(
                        {
                            url: constObj.url.endFix,
                            method: "post",
                            data: that.endFixData,
                            dataType: "json",
                            beforeSend: function(XMLHttpRequest) {
                                XMLHttpRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                            },
                            success: function (data) {
                                that.endFixAssist.isInProcess = false;

                                if (data.code !== "200") {
                                    alert(data.message);
                                } else {
                                    that.getFixList(that.query);
                                    $(".prompt-end").modal("hide");
                                }
                            },
                            error: function () {
                                that.endFixAssist.isInProcess = false;
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
                    queryClone["pageNum"] = queryClone.pageNum -1;

                    this.getFixList(queryClone);
                },
                nextPageHandle: function() {
                    if (this.pageBar.noNextPage) {
                        return;
                    }

                    var queryClone = JSON.parse(JSON.stringify(this.query));
                    queryClone["pageNum"] = queryClone.pageNum + 1;

                    this.getFixList(queryClone);
                },
                getFixList: function(queryData) {
                    var that = this;
                    $.ajax(
                        {
                            url: constObj.url.fixListData,
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
                                    that.fixList = data.result.content;
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
                clearAddOrEditData: function() {
                    this.addOrEditData.id = "";
                    this.addOrEditData.fixName = "";
                    this.addOrEditData.chargePerson = "";
                }
            },
            created: function() {
                this.getFixList(this.query);
            }
        }
    );
</script>
</body>
</html>
