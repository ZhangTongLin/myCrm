<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-员工管理</title>
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.css">
    <%@include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/left.jsp">
        <jsp:param name="menu" value="admin"/>
    </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-2">
                    <div class="box">
                        <div class="box-body">
                            <button id="addDept" class="btn btn-default">添加部门</button>
                            <a href="javascript:;" id="deleteDeptBtn" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除部门</a>
                            <input type="hidden" id="deptId">
                            <ul id="ztree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-10">
                    <!-- Default box -->
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">员工管理</h3>
                            <div class="box-tools pull-right">
                                <button type="button" id="addStaff" class="btn btn-box-tool"  title="Collapse">
                                    <i class="fa fa-plus" ></i> 添加员工
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="box-body">
                                <table class="table" id="dataTable">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>姓名</th>
                                        <th>部门</th>
                                        <th>手机</th>
                                        <th>#</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- /.box -->
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%--添加账号模态框--%>
    <!-- Modal -->
    <div class="modal fade" id="addStaffModel" >
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加账号</h4>
                </div>
                <div class="modal-body">
                    <form id="addStaffForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" class="form-control" name="userName">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" class="form-control" name="phoneNum">
                        </div>
                        <div class="form-group">
                            <label>密码(默认000000)</label>
                            <input type="password" class="form-control" name="password" value="000000">
                        </div>
                        <div class="form-group">
                            <label>所属部门</label>
                            <div id="checkboxList"></div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="addStaffBtn">保存</button>
                </div>
            </div>
        </div>
    </div>
    <%--添加账号模态框结束--%>
    <%@include file="../include/footBar.jsp"%>
</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/tree/js/jquery.ztree.all.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function(){

        var dataTable = $("#dataTable").DataTable({
            "processing": true,
            "serverSide": true,
            "ajax": {
                "url" : "/admin/load.json",
                "data" : function(data){
                    data.deptId = $("#deptId").val();
                }
            },
            "lengthChange": false,
            "pageLength": 10,
            "columns":[
                {"data":"id"},
                {"data":"userName"},
                {"data":function(row){
                    var deptArray = row.deptList;
                    var str = "";
                    for(var i = 0;i < deptArray.length;i++) {
                        str += deptArray[i].deptName + " ";
                    }
                    return str;
                }},
                {"data":"phoneNum"},
                {"data":function(row){
                    return "<a href='javascript:;' rel='"+row.id+"' class='delStaffLink'>删除</a>";
                }}
            ],
            "columnDefs": [
                {
                    "targets": [2,3,4],
                    "orderable": false
                },
                {
                    "targets": [0],
                    "visible": false
                }
            ],
            language:{
                "search":"账号:",
                "info": "显示从 _START_ 到 _END_ 条数据，共 _TOTAL_ 条",
                "infoEmpty":"没有任何数据",
                "emptyTable":"暂无数据",
                "processing":"加载中...",
                "paginate": {
                    "first":      "首页",
                    "last":       "末页",
                    "next":       "下一页",
                    "previous":   "上一页"
                }
            }
        });


        //添加员工
        $("#addStaff").click(function () {
            $("#checkboxList").html("");
            $.get("/admin/dept.json").done(function (data) {

                for (var i = 0; i<data.length; i++) {
                    var obj = data[i];
                    if(obj.id != 1) {
                        var html = '<label class="checkbox-inline"><input type="checkbox" name="deptId" value="'+obj.id+'">'+obj.deptName+'</label>';
                        $("#checkboxList").append(html);
                    }
                }
                $("#addStaffModel").modal({
                    show:true,
                    backdrop:'static'
                });
            }).error(function () {
                layer.msg("获取部门信息失败");
            });
        });

        $("#addStaffBtn").click(function () {
            $("#addStaffForm").submit();
        })
        $("#addStaffForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                userName:{
                    required:true
                },
                phoneNum:{
                    required:true
                },
                password:{
                    required:true
                },
                deptId:{
                    required:true
                }
                
            },
            messages : {
                userName:{
                    required:"请输入账号"
                },
                phoneNum:{
                    required:"请输入手机号"
                },
                password:{
                    required:"请输入密码"
                },
                deptId:{
                    required:"请选择部门"
                }
            },
            submitHandler : function () {
                $.post("/staff/new",$("#addStaffForm").serialize()).done(function (data) {


                    if (data.state == "success") {
                        $("#addStaffModel").modal('hide');
                        layer.msg("添加成功");
                        dataTable.ajax.reload();
                    } else {
                        layer.msg(data.message);
                    }

                }).error(function () {
                    layer.msg("系统异常，请稍后再试");
                });
            }
        })


        //删除员工
        $(document).delegate(".delStaffLink","click" , function () {

            var id =  $(this).attr("rel");

            layer.confirm("确定要删除吗？",function (index) {
                $.post("/staff/"+ id +"/delete").done(function (data) {
                    layer.close(index);
                    if (data.state == "success") {
                        layer.msg("删除成功");
                        dataTable.ajax.reload();
                    } else {
                        layer.msg(data.message);
                    }

                }).error(function () {
                    layer.msg("系统异常，请稍后再试");
                });
            });
        });

        //删除部门
        $("#deleteDeptBtn").click(function () {
            layer.confirm("确定要删除吗？",function () {
                $.get("/admin/dept/delete").done(function (json) {

                }).error(function () {
                    layer.msg(json.message);
                });
            });
        });

        //添加部门
        $("#addDept").click(function () {
            layer.prompt({title : "添加部门"},function (text,index) {
                layer.close(index);
                $.post("/admin/dept/new",{deptName : text}).done(function (data) {
                    if (data.state == "success") {
                        layer.msg(data.state);
                        layer.msg("添加部门成功");
                        //刷新树
                        var treeObj = $.fn.zTree.getZTreeObj("ztree");
                        treeObj.reAsyncChildNodes(null, "refresh");
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("服务器异常，请稍后再试");
                });
            });
        });
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                },
             key : {

                 name : "deptName",
             }
            },
            async:{
                enable:true,
                url:"/admin/dept.json",
                type:"get",
                dataFilter:ajaxDataFilter
            },
            callback:{
                onClick:function(event,treeId,treeNode,clickFlag){
                    $("#deptId").val(treeNode.id);
                    dataTable.ajax.reload();
                }
            }
        };

        function ajaxDataFilter(treeId, parentNode, responseData) {
            if (responseData) {
                for(var i =0; i < responseData.length; i++) {
                    if(responseData[i].id == 1) {
                        responseData[i].open = true;
                        break;
                    }
                }
            }
            return responseData;
        }

        $.fn.zTree.init($("#ztree"), setting);
    });
</script>
</body>
</html>
