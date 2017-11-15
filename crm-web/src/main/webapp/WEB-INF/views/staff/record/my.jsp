<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的记录</title>
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
        <jsp:param name="menu" value="record_my"/>
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
                    <h3 class="box-title">我的销售机会</h3>

                    <div class="box-tools pull-right">
                        <a href="/staff/my/record/new" type="button" class="btn btn-box-tool">
                            <i class="fa fa-plus"></i> 添加机会
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <td>机会名称</td>
                                <td>关联客户</td>
                                <td>机会价值</td>
                                <td>当前进度</td>
                                <td>最后跟进时间</td>
                            </tr>
                        </thead>
                        <tbody>
                            <div>
                                <c:if test="${empty pageInfo.list}">
                                    您暂时没有添加记录，可以<a href="/staff/my/record/new">添加机会</a>
                                </c:if>
                            </div>
                            <c:forEach items="${pageInfo.list}" var="record">
                                <tr class="chanceDetail" rel="${record.id}">
                                    <td><a href="/staff/my/record/${record.id}">${record.name}</a></td>
                                    <td><a href="/customer/my/show/${record.customer.id}">${record.customer.custName}</a></td>
                                    <td>${record.worth}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${record.progress == '成交'}">
                                                <span class="label label-success">${record.progress}</span>
                                            </c:when>
                                            <c:when test="${record.progress == '暂时搁置'}">
                                                <span class="label label-danger">${record.progress}</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${record.progress}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span title="<fmt:formatDate value="${record.customer.lastContactTime}"/>"><fmt:formatDate value="${record.customer.lastContactTime}" pattern="MM月dd日"/></span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <ul id="pagination" class="pagination pagination-lg"></ul>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
<%@include file="../../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/pageHelper/jquery.twbsPagination.js"></script>
<script>
    //分页插件
    $("#pagination").twbsPagination({
        totalPages:"${pageInfo.pages}",
        visiblePages:5,
        href:"?p={{number}}",
        first: "首页",
        prev: "上一页",
        next:"下一页",
        last:"末页"
    });
    $(function () {
        $(".chanceDetail").click(function () {

            var id = $(this).attr("rel");
           window.location.href="/staff/my/record/"+id;
        });
    });
</script>
</body>
</html>
