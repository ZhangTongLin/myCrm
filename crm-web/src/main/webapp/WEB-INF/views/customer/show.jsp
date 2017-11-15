<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-客户资料</title>
   <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="/static/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <style>
        .td_title {
            font-weight: bold;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <%@include file="../include/header.jsp"%>
    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/left.jsp">
        <jsp:param name="menu" value="customer_my"/>
    </jsp:include>
    <!-- =============================================== -->


    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">客户资料</h3>
                    <div class="box-tools">
                        <a href="/customer/my" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <a href="/customer/my/${customer.id}/edit" class="btn bg-purple btn-sm"><i class="fa fa-pencil"></i> 编辑</a>
                        <button id="tranCustomer" class="btn bg-orange btn-sm"><i class="fa fa-exchange"></i> 转交他人</button>
                        <button class="btn bg-maroon btn-sm"><i class="fa fa-recycle"></i> 放入公海</button>
                        <a href="javascript:;" id="deleteBtn" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</a>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">姓名</td>
                            <td>${customer.custName}</td>
                            <td class="td_title">职位</td>
                            <td>${customer.jobTitle}</td>
                            <td class="td_title">联系电话</td>
                            <td>${customer.phoneNum}</td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>${customer.trade}</td>
                            <td class="td_title">客户来源</td>
                            <td>${customer.source}</td>
                            <td class="td_title">级别</td>
                            <td>${customer.level}</td>
                        </tr>
                        <c:if test="${not empty customer.address}">
                            <tr>
                                <td class="td_title">地址</td>
                                <td colspan="5">${customer.address}</td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty customer.mark}">
                            <tr>
                                <td class="td_title">备注</td>
                                <td colspan="5">${customer.mark}</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">创建日期：<span title="<fmt:formatDate value="${customer.createTime}"/>"><fmt:formatDate value="${customer.createTime}" pattern="MM月dd日"/></span> &nbsp;&nbsp;&nbsp;&nbsp;
                        最后修改日期：<span title="<fmt:formatDate value="${customer.updateTime}"/>"><fmt:formatDate value="${customer.updateTime}" pattern="MM月dd日"/></span></span>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="box">
                            <div class="box-header with-border">
                                <h3 class="box-title">跟进记录</h3>
                            </div>
                            <div class="box-body">
                                <ul class="list-group">
                                    <c:forEach items="${recordList}" var="record">
                                        <li class="list-group-item">
                                            <a href="/staff/my/record/${record.id}" target="_blank">${record.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="box">
                            <div class="box-header with-border">
                                <h3 class="box-title">日程安排
                                    <small><button id="addTaskBtn" class="btn btn-success btn-xs"><i class="fa fa-plus"></i></button></small>
                                </h3>
                            </div>
                            <div class="box-body">
                                <c:forEach items="${taskList}" var="task">
                                    <li class="list-group-item">
                                        <a href="/task/wait" target="_blank">${task.title}</a>
                                    </li>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="box">
                            <div class="box-header with-border">
                                <h3 class="box-title">相关资料</h3>
                            </div>
                            <div class="box-body">

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="addTaskModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">新增待办事项</h4>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="/task/new" id="addTaskForm">
                                <input name="staffId" value="${sessionScope.curr_account.id}" type="hidden">
                                <input type="hidden" name="custId" value="${customer.id}">
                                <div class="form-group">
                                    <label>任务名称</label>
                                    <input name="title" type="text" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>完成日期</label>
                                    <input name="finish" type="text" class="form-control" id="datepicker">
                                </div>
                                <div class="form-group">
                                    <label>提醒时间</label>
                                    <input name="remind" type="text" class="form-control" id="datepicker2">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="addTask">添加</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

        </section>
        <!-- /.content -->

    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
   <%@include file="../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>

<script>

    $(function () {
        var customerId = ${customer.id};
        $("#deleteBtn").click(function () {

            layer.confirm("确定要删除吗？",function (index) {
                layer.close(index);
                window.location.href="/customer/my/"+ customerId +"/delete";
            })
        });

        //添加待办事项
        $("#addTaskBtn").click(function () {
            $("#addTaskModal").modal({
                show : true,
                backdrop : 'static'
            });
        });

        $("#addTask").click(function () {
            $("#addTaskForm").submit();
        });

        $("#addTaskForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                title : {
                    required : true
                },
                finish : {
                    required : true
                }
            },
            messages : {
                title : {
                    required : "请输入任务内容"
                },
                finish : {
                    required : "请选择完成时间"
                }
            }
        });

        //日历的插件
        var picker = $('#datepicker').datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true,
            startDate:moment().format("yyyy-MM-dd")
        });
        picker.on("changeDate",function (e) {
            var today = moment().format("YYYY-MM-DD");
            $('#datepicker2').datetimepicker('setStartDate',today);
            $('#datepicker2').datetimepicker('setEndDate', e.format('yyyy-mm-dd'));
        });
        var timepicker = $('#datepicker2').datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true
        });

    });
</script>

</body>
</html>
