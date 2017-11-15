<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
   <%@include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
<%@include file="../include/header.jsp"%>
<%@include file="../include/left.jsp"%>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">修改密码</h3>
                   
                </div>
                <div class="box-body">
                    <form action="">
                        <div class="form-group">
                            <label>原始密码</label>
                            <input type="password" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>新密码</label>
                            <input type="password" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>确认密码</label>
                            <input type="password" class="form-control">
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <button class="btn btn-primary">保存</button>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 1.0
        </div>
        <strong>Copyright &copy; 2010 -2017 <a href="http://almsaeedstudio.com">凯盛软件</a>.</strong> All rights
        reserved.
    </footer>

</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 -->
<script src="../../../../../../../../../kaisheng/凯盛/20-crm/CRM-2017/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="../../../../../../../../../kaisheng/凯盛/20-crm/CRM-2017/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="../../../../../../../../../kaisheng/凯盛/20-crm/CRM-2017/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="../../../../../../../../../kaisheng/凯盛/20-crm/CRM-2017/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="../../../../../../../../../kaisheng/凯盛/20-crm/CRM-2017/dist/js/app.min.js"></script>
</body>
</html>
