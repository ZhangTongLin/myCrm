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
                                <input type="checkbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
                                <span class="text">${task.title}</span>
                                <small class="label ${task.overdue ? 'label-success':'label-danger'}"><i class="fa fa-clock-o"></i><fmt:formatDate value="${task.finishTime}" pattern="yyyy年MM月dd日"/></small>
                                <div class="tools">
                                    <i class="fa fa-edit"></i>
                                    <i class="fa fa-trash-o deleteTask" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>
                        <li class="done">
                            <input type="checkbox">
                            <span class="text">给张三打电话联系</span>
                            <a href=""><i class="fa fa-user-o"></i> 张三</a>
                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 7月15日</small>
                            <div class="tools">
                                <i class="fa fa-edit"></i>
                                <i class="fa fa-trash-o"></i>
                            </div>
                        </li>
                        <li>
                            <input type="checkbox">
                            <span class="text">给张三打电话联系</span>
                            <a href=""><i class="fa fa-money"></i> 9号楼23#</a>
                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 8月3日</small>
                            <div class="tools">
                                <i class="fa fa-edit"></i>
                                <i class="fa fa-trash-o"></i>
                            </div>
                        </li>

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

<script>
    $(function () {
       $(".deleteTask").click(function () {
           var id = $(this).attr("rel");
           alert(id);
       })
    });
</script>
</body>
</html>
