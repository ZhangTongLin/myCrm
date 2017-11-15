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
                                <h3 class="box-title">日程安排</h3>
                            </div>
                            <div class="box-body">

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

<script>

    $(function () {
        var customerId = ${customer.id};
        $("#deleteBtn").click(function () {

            layer.confirm("确定要删除吗？",function (index) {
                layer.close(index);
                window.location.href="/customer/my/"+ customerId +"/delete";
            })
        });

    });
</script>

</body>
</html>
