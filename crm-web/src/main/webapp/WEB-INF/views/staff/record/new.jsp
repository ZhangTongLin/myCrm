<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-添加销售机会</title>
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

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增销售机会</h3>

                    <div class="box-tools pull-right">
                        <a href="/staff/my/record" type="button" class="btn btn-primary">返回列表</a>
                    </div>
                </div>
                <div class="box-body">
                    <form id="newChanceForm" method="post">
                        <input value="${sessionScope.curr_account.id}" type="hidden" name="staffId"/>
                        <div class="form-group">
                            <label>机会名称</label>
                            <input name="name" type="text" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>关联客户</label>
                            <select name="custId" id="" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option value="${customer.id}">${customer.custName}(${customer.phoneNum})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>机会价值</label>
                            <input name="worth" type="text" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>当前进度</label>
                            <select name="progress" class="form-control">
                                <c:forEach items="${progressList}" var="progress">
                                    <option value="${progress}">${progress}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>详细内容</label>
                            <textarea name="content" class="form-control"></textarea>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button id="newChanceBtn" class="btn btn-primary">保存</button>
                </div>
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
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function () {
        $("#newChanceBtn").click(function () {
           $("#newChanceForm").submit();
        });
        $("#newChanceForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                name : {
                    required : true
                },
                custId : {
                    required : true
                },
                worth : {
                    required : true,
                    number : true,
                    min : 1
                },
                progress : {
                    required : true
                }
            },
            messages : {
                name : {
                    required : "请输入机会名称"
                },
                custId : {
                    required : "请选择客户"
                },
                worth : {
                    required : "请输入正确的价值金额",
                    number : "请输入正确的价值金额",
                    min : "请输入正确的价值金额"
                },
                progress : {
                    required : "请选择当前进度"
                }
            }
        });
    });
</script>
</body>
</html>
