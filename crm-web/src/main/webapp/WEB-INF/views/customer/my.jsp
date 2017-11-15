<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的客户</title>
   <%@include file="../include/css.jsp"%>
    <style>
        .name-avatar {
            display: inline-block;
            width: 50px;
            height: 50px;
            background-color: #ccc;
            border-radius: 50%;
            text-align: center;
            line-height: 50px;
            font-size: 24px;
            color: #FFF;
        }
        .table>tbody>tr:hover {
            cursor: pointer;
        }
        .table>tbody>tr>td {
            vertical-align: middle;
        }
        .star {
            font-size: 20px;
            color: #ff7400;
        }
        .pink {
            background-color: #a77a94;
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
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的客户</h3>
                    <div class="box-tools pull-right">
                        <a href="/customer/my/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增客户</a>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fa fa-file-excel-o"></i> 导出Excel <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="/customer/my/export.xls">导出为xls文件</a></li>
                                <li><a href="/customer/my/export.csv">导出为csv文件</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <tbody>
                            <tr>
                                <th width="80"></th>
                                <th>姓名</th>
                                <th>职位</th>
                                <th>跟进时间</th>
                                <th>级别</th>
                                <th>联系方式</th>
                            </tr>
                            <c:if test="${empty pageInfo.list}">
                                <tr>
                                    <td colspan="6">你还没有客户，可以 <a href="/customer/my/new">新增客户</a></td>
                                </tr>
                            </c:if>
                            <c:forEach items="${pageInfo.list}" var="customer">
                                <tr class="customerShow" rel="${customer.id}">
                                    <td><span class="name-avatar ${customer.sex == '女士' ? 'pink':''}">${fn:substring(customer.custName,0 ,1 )}</span></td>
                                    <td>${customer.custName}</td>
                                    <td>${customer.jobTitle}</td>
                                    <td><fmt:formatDate value="${customer.lastContactTime}"/></td>
                                    <td class="star">${customer.level}</td>
                                    <td><i class="fa fa-phone"></i> ${customer.phoneNum} <br></td>
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
   <%@include file="../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/plugins/pageHelper/jquery.twbsPagination.js"></script>
<script>
    $(function () {
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

        //客户详情
        $(".customerShow").click(function () {
            var id = $(this).attr("rel");
            window.location.href="/customer/my/show/" + id;
        })
    });
</script>
</body>
</html>
