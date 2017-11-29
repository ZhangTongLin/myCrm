<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>凯盛软件CRM-客户统计</title>
  <%@include file="include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
<%@include file="include/header.jsp"%>
  <jsp:include page="include/left.jsp">
    <jsp:param name="menu" value="chart"/>
  </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">客户级别数量统计</h3>
                </div>
                <div class="box-body">
                    <div id="bar" style="height: 300px;width: 100%"></div>
                </div>
                <div class="box-body">
                    <div id="bar1" style="height: 500px;width: 100%"></div>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
<%@include file="include/footBar.jsp"%>
</div>
<!-- ./wrapper -->
<%@include file="include/js.jsp"%>
<script src="/static/plugins/echarts/echarts.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>
  $(function () {

      //柱状图
      var bar = echarts.init(document.getElementById("bar"));
      var option = {
          title: {
              text: "每月新增客户数量统计",
              left: 'center'
          },
          tooltip: {},
          legend: {
              data: ['新增人数'],
              left: 'right'
          },
          xAxis: {
              type: 'category',
              data: []
          },
          yAxis: {},
          series: {
              name: "人数",
              type: 'bar',
              data:[]
          }
      }

      bar.setOption(option);

      $.get("/chart/count/increase").done(function (json) {
            var array = json.data;
            var month = [];
            var count = [];

            for (var i = 0; i < array.length; i++) {
                var obj = array[i];
                month.push(obj.months);
                count.push(obj.increase);
            }
            bar.setOption({
                xAxis: {
                    data: month
                },
                series : {
                  data : count
              }
          });
      }).error(function () {
          layer.msg("加载数据异常");
      });

      //漏斗图
      var bar1 = echarts.init(document.getElementById("bar1"));

      var option1 = {
          title: {
              text: "客户发展趋势图",
              left: 'left'
          },
          series: [
              {
                  name:'漏斗图',
                  type:'funnel',
                  left: '20%',
                  width: '50%',
                  label: {
                      normal: {
                          show: true,
                          position: 'inside'
                      }
                  }
              }
          ]
      }
      bar1.setOption(option1);

      $.get("/chart/progress").done(function (json) {

          if (json.state == "success") {
              var array = json.data;

              var progressArray = [];
              var data1 = [];

              for (var i = 0; i < array.length; i++) {
                  var  obj = array[i];
                  var c = obj.counts
                  progressArray.push(obj.progress);
                  data1.push({value : c ,name : obj.progress});
              }
              bar1.setOption({
                  legend: {
                      data: progressArray
                  },
                  series : [
                      {
                         data : data1
                      }
                  ]
              });

          } else {
              layer.msg("获取数据异常");
          }

      }).error(function () {
          layer.msg("系统异常，请稍后再试");
      });
  });
</script>
</body>
</html>
