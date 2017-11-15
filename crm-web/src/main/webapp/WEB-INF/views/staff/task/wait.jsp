<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-待办事项</title>
    <%@include file="../../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../../include/header.jsp"%>
    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../../include/left.jsp">
        <jsp:param name="menu" value="log_wait"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">计划任务</h3>

                    <div class="box-tools pull-right">
                        <a href="/task/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</a>
                        <button class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</button>
                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">
                        <c:forEach items="${taskList}" var="task">
                            <li class="${task.done == 1 ? 'done' : ''}">
                                <input class="task_checkbox" type="checkbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
                                <span class="text">${task.title}</span>
                                <small class="label ${task.overdue ? 'label-success':'label-danger'}"><i class="fa fa-clock-o"></i><fmt:formatDate value="${task.finishTime}" pattern="yyyy年MM月dd日"/></small>
                                <div class="tools">
                                    <i class="fa fa-edit"></i>
                                    <i class="fa fa-trash-o deleteTask" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%@include file="../../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>

<script>
    //删除
    $(function () {
       $(".deleteTask").click(function () {
           var id = $(this).attr("rel");
           layer.confirm("确定要删除吗？",function () {
                window.location.href="/task/" + id +"/delete";
           });
       });
       $(".task_checkbox").click(function () {
           var id = $(this).val();
           var checked = $(this)[0].checked;
           layer.confirm("确定将该事项标记为已做吗？",function () {
               if (checked) {
                   window.location.href="/task/"+id+"/done";
               }
           });

       });
    });
</script>
</body>
</html>
