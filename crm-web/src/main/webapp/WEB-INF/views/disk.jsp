<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>凯盛软件CRM-公司网盘</title>
  <%@include file="include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
  <style>
    tr{
      height: 50px;
      line-height: 50px;
    }
    .table>tbody>tr>td{
      vertical-align: middle;
    }
    .file_icon {
      font-size: 30px;
      text-align: center;
    }
    .table>tbody>tr:hover{
      cursor: pointer;
    }
    .webuploader-container {
      display: inline-block;
    }
    .webuploader-pick {
      padding: 5px 10px;
      overflow: visible;
      font-size: 12px;
      line-height:1.5;
      font-weight: 400;
    }
  </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
<%@include file="include/header.jsp"%>
  <jsp:include page="include/left.jsp">
    <jsp:param name="menu" value="disk"/>
  </jsp:include>

  <!-- 右侧内容部分 -->
  <div class="content-wrapper">

    <!-- Main content -->
    <section class="content">
      <!-- Default box -->
      <div class="box">
        <div class="box-header with-border">
          <h3 class="box-title">
              <c:if test="${empty disk}">
                  公司网盘
              </c:if>
              ${disk.name}
          </h3>

          <div class="box-tools pull-right">
              <c:if test="${not empty disk}">
                  <a href="/disk?id=${disk.pId}" class="btn btn-default btn-sm"><i class="fa fa-arrow-left"></i> 返回上级</a>
              </c:if>
            <c:choose>
              <c:when test="${disk.type == 'file'}">
                  <a href="/disk/download?_=${disk.id}&fileName=${disk.name}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
              </c:when>
              <c:otherwise>
                  <div id="picker"><i class="fa fa-upload"></i> 上传文件</div>
                  <button id="addFileBtn" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新建文件夹</button>
              </c:otherwise>
          </c:choose>
          </div>
        </div>
        <div class="box-body no-padding">

          <table class="table table-hover">
            <tbody id="dataTable">
            <c:if test="${disk.type == 'file'}">
                <tr>
                    <td colspan="4">
                        <c:choose>
                            <c:when test="${disk.name.endsWith('.pdf') or disk.name.endsWith('.jpg') or disk.name.endsWith('.png') or disk.name.endsWith('.bmp') or disk.name.endsWith('.gif') or disk.name.endsWith('.txt')}">
                                <a href="/disk/download?id=${disk.id}" target="_blank" class="btn btn-sm btn-info"><i class="fa fa-search"></i> 在线预览</a>
                                <a href="/disk/download?id=${disk.id}&fileName=${disk.name}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/disk/download?id=${disk.id}&fileName=${disk.name}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:if>
              <c:if test="${empty diskList}">
                <tr><td colspan="4">暂无内容</td></tr>
              </c:if>
              <c:forEach items="${diskList}" var="disk">
                <tr class="tr" rel="${disk.id}">
                  <td width="50" class="file_icon">
                    <c:choose>
                      <c:when test="${disk.type == 'dir'}">
                        <i class="fa fa-folder-o"></i>
                      </c:when>
                      <c:otherwise>
                        <i class="fa fa-file-o"></i>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>${disk.name}</td>
                  <td><fmt:formatDate value="${disk.updateTime}" pattern="MM月dd日"/></td>
                  <td width="100">
                    <c:if test="${disk.type == 'file'}">
                      ${disk.fileSize}
                    </c:if>
                  </td>
                  <td width="150">
                    <div class="btn-group">
                      <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-ellipsis-h"></i>
                      </button>
                      <ul class="dropdown-menu">
                          <c:choose>
                              <c:when test="${disk.type == 'file'}">
                                  <li><a href="/disk/download?id=${disk.id}&fileName=${disk.name}">下载</a></li>
                                  <li><a href="/disk/download?id=${disk.id}" target="_blank">在线预览</a></li>
                              </c:when>
                              <c:otherwise>
                                  <li><a href="/disk?id=${disk.id}">打开</a></li>
                              </c:otherwise>
                          </c:choose>
                        <li rel="${disk.id}" class="rename"><a>重命名</a></li>
                        <li rel="${disk.id}" class="delete"><a>删除</a></li>
                      </ul>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->

      <div class="modal fade" id="addFileModal">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">新建文件夹</h4>
            </div>
            <div class="modal-body">
              <form id="addDiskForm">
                <input type="hidden" name="pId" value="${disk.id}">
                <input type="hidden" name="staffId" value="${sessionScope.curr_account.id}">
                <h5>请输入文件夹名</h5>
                <input name="name" type="text" class="form-control">
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button type="button" class="btn btn-primary" id="saveDisk">新建</button>
            </div>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
      </div><!-- /.modal -->


    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
<%@include file="include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="include/js.jsp"%>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/art-template/art-template.js"></script>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>
<script type="text/template" id="trTemplate">
    <%--https://aui.github.io/art-template/--%>
    <tr class="tr" rel="{{id}}">
        <td width="80" class="file_icon">
            <? if(type == 'file') { ?>
            <i class="fa fa-file-o"></i>
                <? } else if(type == 'dir') { ?>
            <i class="fa fa-folder-o"></i>
                <?}?>
        </td>
        <td>{{name}}</td>
        <td>{{updateTime}}</td>
        <td width="100">{{fileSize}}</td>
        <td width="100">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-ellipsis-h"></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="">下载</a></li>
                    <li><a href="#">重命名</a></li>
                    <li><a href="#">删除</a></li>
                </ul>
            </div>
        </td>
    </tr>
</script>
<script>
  $(function () {

      var pId = ${not empty requestScope.disk ? requestScope.disk.id : '0'};
      var staffId = ${sessionScope.curr_account.id}

      //w文件上传
      var uploader = WebUploader.create({
          pick : '#picker',
          swf:'/static/plugins/webuploader/Uploader.swf',
          server : "/disk/upload",//上传路径
          auto : true,//自动上传
          fileVal : 'file',//上传文件的表单控件的名称
          formData : {
              "pId" : pId,
              "staffId" : staffId
          }//发送请求给服务的额外数据
      });
      var loadIndex = -1;

      uploader.on('uploadStart',function (file) {
          loadIndex = layer.load(2);
      });//开始上传

      uploader.on('uploadSuccess',function (file,data) {

          if (data.state == 'success') {
              layer.msg("上传成功");
              $("#dataTable").html("");
              for (var i = 0;i < data.data.length; i++) {
                  var obj = data.data[i];
                  obj.updateTime = moment(obj.updateTime).format("MM月DD日");
                  var html = template('trTemplate',obj);
                  $("#dataTable").append(html);
              }
          }

      });//上传成功

      uploader.on('uploadError',function (file) {
          layer.msg("上传失败，服务器异常");
      });//上传失败

      uploader.on('uploadComplete',function (file) {
          layer.close(loadIndex);
      });//无论上传成功还是失败

      //新建文件夹
      $("#addFileBtn").click(function () {
          $("#addFileModal").modal({
              show : true,
              backdrop : 'static'
          });
      });
      $("#saveDisk").click(function (index) {

          $("#addDiskForm").submit();
      });
      $("#addDiskForm").validate({
          errorClass : "text-danger",
          errorElement : "span",
          rules : {
              name : {
                  required : true
              }
          },
          messages : {
              name : {
                  required : "文件夹名不能为空"
              }
          },
          submitHandler : function (){
              $.post("/disk/new",$("#addDiskForm").serialize()).done(function (data) {
                  if (data.state == 'success') {
                      $("#addFileModal").modal('hide');
                      layer.msg("创建成功");
                      $("#dataTable").html("");
                      for (var i = 0; i < data.data.length;i ++) {
                            var obj = data.data[i];
                            obj.updateTime = moment(obj.updateTime).format("MM月DD日");
                            var html = template("trTemplate",obj);
                            $("#dataTable").append(html);
                      }

                  } else {
                      layer.msg(data.message);
                  }
              }).error(function () {
                  layer.msg("系统异常，请稍后再试");
              });
          }
      });
      //行点击
      $(document).delegate(".tr","click",function () {
          var id = $(this).attr("rel");
          window.location.href="/disk?id="+id;
      });

      //删除文件

      $(".delete").click(function (e) {
          e.stopPropagation();
          var diskId = $(this).attr("rel")
          layer.confirm("确定要删除吗？",function () {
             $.get("/disk/delete/" + diskId).done(function (json) {
                 if (json.state == "success") {
                     layer.msg("删除成功");
                     window.history.go(0);
                 } else {
                     layer.msg(json.message);
                 }
             }).error(function () {
                 layer.msg("系统异常，请稍后再试");
             });
          });
      });
      //重命名
      $(".rename").click(function (e) {
          e.stopPropagation();
          var diskId = $(this).attr("rel");
          layer.prompt({title : "请输入新的名称"},function (text) {
              $.get("/disk/rename",{"diskId" : diskId,"name" : text}).done(function (json) {

                  if (json.state == "success") {
                    layer.msg("修改成功");
                    window.history.go(0);
                  } else {
                      layer.msg("修改失败");
                  }

              }).error(function () {
                  layer.msg("系统异常，请杀后再试");
              });
          });
      });
  });
</script>
</body>
</html>
