<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-机会详情</title>
<%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="/static/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <style>
        .td_title {
            font-weight: bold;
        }
        .star {
            font-size: 20px;
            color: #ff7400;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../../include/header.jsp"%>
    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../../include/left.jsp">
        <jsp:param name="menu" value="record_my"/>
    </jsp:include>
    <!-- =============================================== -->


    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">销售机会基本资料</h3>
                    <div class="box-tools">
                        <a href="/staff/my/record" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <button id="delBtn" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">机会名称</td>
                            <td>${record.name}</td>
                            <td class="td_title">价值</td>
                            <td><fmt:formatNumber value="${record.worth}"/> </td>
                            <td class="td_title">当前进度</td>
                            <td>
                                ${record.progress}
                                <button class="btn btn-xs btn-success" id="showChangeProgressModalBtn"><i class="fa fa-pencil"></i></button>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">
                        创建日期：<span title="<fmt:formatDate value="${record.createTime}"/>"><fmt:formatDate value="${record.createTime}" pattern="MM月dd日"/></span>
                    </span>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">关联客户资料</h3>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">姓名</td>
                            <td>${record.customer.custName}</td>
                            <td class="td_title">职位</td>
                            <td>${record.customer.jobTitle}</td>
                            <td class="td_title">联系电话</td>
                            <td>${record.customer.phoneNum}</td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>${record.customer.trade}</td>
                            <td class="td_title">客户来源</td>
                            <td>${record.customer.source}</td>
                            <td class="td_title">级别</td>
                            <td class="star">${record.customer.level}</td>
                        </tr>

                        <tr>
                            <td class="td_title">地址</td>
                            <td colspan="5">${record.customer.address}</td>
                        </tr>


                        <tr>
                            <td class="td_title">备注</td>
                            <td colspan="5">${record.customer.mark}</td>
                        </tr>

                    </table>
                </div>
            </div>

            <div class="row">
                <div class="col-md-8">
                    <h4>跟进记录
                        <small><button id="showRecordModalBtn" class="btn btn-success btn-xs"><i class="fa fa-plus"></i></button></small>
                    </h4>
                    <ul class="timeline">
                        <c:forEach items="${followRecord}" var="progress">
                            <c:choose>
                                <c:when test="${progress.content} == '将进度修改为:[成交]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-check bg-green"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${progress.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:when test="${progress.content} == '将进度修改为:[暂时搁置]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-close bg-red"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${progress.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-info bg-blue"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${progress.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
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

            <div class="modal fade" id="recordModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">添加跟进记录</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/staff/my/record/progress/update" id="recordForm" method="post">
                                <input type="hidden" name="saleId" value="${record.id}">
                                <textarea id="recordContent"  class="form-control" name="content"></textarea>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveRecordBtn">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

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
                                <input type="hidden" name="recordId" value="${record.id}">
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

            <div class="modal fade" id="changeProgressModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">更新当前进度</h4>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="/staff/my/record/${record.id}/progress/update" id="updateProgressForm">
                                <input type="hidden" name="saleId" value="${record.id}">
                                <select name="progress" class="form-control">
                                    <c:forEach items="${progressList}" var="progress">
                                        <option value="${progress}">${progress}</option>
                                    </c:forEach>
                                </select>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveProgress">更新</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
<%@include file="../../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script>
    $(function () {
        //更改状态
        $("#showChangeProgressModalBtn").click(function () {
            $("#changeProgressModal").modal({
                show : true,
                backdrop : 'static'
            });
        });
        $("#saveProgress").click(function () {
            $("#updateProgressForm").submit();
        });
        $("#showRecordModalBtn").click(function () {
            $("#recordModal").modal({
                show :true,
                backdrop : 'static'
            });
        });
        //添加新纪录
        $("#saveRecordBtn").click(function () {
            $("#recordForm").submit();
        });
        $("#recordForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                content : {
                    required : true
                }
            },
            messages : {
                content : {
                    required : "请输入内容"
                }
            }
        });
        //删除机会
        $("#delBtn").click(function () {
            layer.confirm("确定要删除吗",function () {
                var id = ${record.id};
                window.location.href="/staff/my/record/"+id+"/delete";
            });
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
