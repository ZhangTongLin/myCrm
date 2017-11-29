<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>凯盛软件CRM</title>
  <link rel="stylesheet" href="/static/plugins/iCheck/square/blue.css">
  <%@include file="include/css.jsp"%>
</head>
<body class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <a href="#"><b>凯盛软件</b></a>
  </div>
  <!-- /.login-logo -->
  <div class="login-box-body">
    <h4><p class="login-box-msg">员工登录</p></h4>
    <c:if test="${not empty message}">
      <p class="login-box-msg" style="color: #9f191f">${message}</p>
    </c:if>

    <form method="post">
      <div class="form-group has-feedback">
        <input name="userName" type="text" class="form-control" placeholder="工号或者手机号" value="lucy">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input name="password" type="password" class="form-control" placeholder="密码" value="123123">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <div class="col-xs-8">
          <div class="checkbox icheck">
            <label class="">
              <div class="icheckbox_square-blue">
                <input type="checkbox" value="true" name="rememberMe">
              </div>
              记住我
            </label>
          </div>
        </div>
        <!-- /.col -->
        <div class="col-offset-8 col-xs-4">
          <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
        </div>
        <!-- /.col -->
      </div>
    </form>

    

  </div>
  <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<!-- jQuery 2.2.3 -->
<script src="/static/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/plugins/iCheck/icheck.min.js"></script>
<script>
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
</script>
</body>
</html>
