<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-添加客户</title>
   <%@include file="../include/css.jsp"%>
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

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增客户</h3>
                    <div class="box-tools pull-right">
                        <a href="/customer/my" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                    </div>
                </div>
                <div class="box-body">
                    <form id="addCustomerForm">
                        <input type="hidden" name="staffId" value="${sessionScope.curr_account.id}">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" name="custName" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>性别</label>
                            <div>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" value="先生" checked> 先生
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" value="女士"> 女士
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>职位</label>
                            <input type="text" name="jobTitle" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>联系方式</label>
                            <input type="text" name="phoneNum" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>地址</label>
                            <input type="text" name="address" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>所属行业</label>
                            <select class="form-control" name="trade">
                                <option value=""></option>
                                <c:forEach items="${trades}" var="trade">
                                    <option value="${trade}">${trade}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>客户来源</label>
                            <select name="source" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${sources}" var="source">
                                    <option value="${source}">${source}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>级别</label>
                            <select class="form-control" name="level">
                                <option value=""></option>
                                <option value="★">★</option>
                                <option value="★★">★★</option>
                                <option value="★★★">★★★</option>
                                <option value="★★★★">★★★★</option>
                                <option value="★★★★★">★★★★★</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>备注</label>
                            <input type="text" name="mark" class="form-control">
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <button id="addCustomerBtn" class="btn btn-primary">保存</button>
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
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>

    $(function () {
        $("#addCustomerBtn").click(function () {
            $("#addCustomerForm").submit();
        });
        $("#addCustomerForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                custName : {
                    required : true
                },
                phoneNum : {
                    required : true
                },
                level : {
                    required : true
                }
            },
            messages : {
                custName : {
                    required : "请输入名称"
                },
                phoneNum : {
                    required : "请输入手机号"
                },
                level : {
                    required : "请选择意向等级"
                }
            },
            submitHandler : function () {
                $.post("/customer/my/new",$("#addCustomerForm").serialize()).done(function (data) {
                    if (data.state == "success") {
                        layer.msg("客户添加成功");
                        window.location.href="/customer/my";
                    } else {
                        layer.confirm(data.message + ",是否要强制添加",{
                            btn: ['强制添加','取消']
                        },function () {
                            $.post("/customer/my/new/compel",$("#addCustomerForm").serialize()).done(function (json) {
                                if (json.state == "success") {
                                    layer.msg("客户添加成功");
                                    window.location.href="/customer/my";
                                } else {
                                    layer.msg(json.message);
                                }
                            }).error(function () {
                                layer.msg("系统异常，请稍后再试");
                            });
                        });
                    }
                }).error(function () {
                    layer.msg("系统异常，请稍后再试");
                });
            }
        });
    });

</script>

</body>
</html>
