<!DOCTYPE HTML>
<html>
<head>
<title>统计信息</title>
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Trotting Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<link href='http://ajax.useso.com/css?family=Roboto:400,100,300,500,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="css/flexslider.css" type="text/css" media="screen" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/magnific-popup.css">
<script type="text/javascript" src="js/move-top.js"></script>
       <script type="text/javascript" src="js/easing.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function($) {
				$(".scroll").click(function(event){		
					event.preventDefault();
					$('html,body').animate({scrollTop:$(this.hash).offset().top},1200);
				});
			});
		</script>
		<script type="text/javascript">
		$(document).ready(function() {
				/*
				var defaults = {
				containerID: 'toTop', // fading element id
				containerHoverID: 'toTopHover', // fading element hover id
				scrollSpeed: 1200,
				easingType: 'linear' 
				};
				*/
		$().UItoTop({ easingType: 'easeOutQuart' });
});
</script>
<script type="text/javascript" src="js/esl.js"></script>

</head>

<body>
<!-- header -->
	<div class="banner1">
		<div class="container">
			<?php include 'components/guide.php';?>
		</div>
	</div>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:400px"></div>
     <script type="text/javascript">
        // 路径配置
        require.config({
            paths:{ 
                'echarts' : 'js/echarts',
                'echarts/chart/pie' : 'js/echarts'
            }
        });
        
        
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main')); 
                
                option = {
                	    title : {
                	        text: '某站点用户访问来源',
                	        subtext: '纯属虚构',
                	        x:'center'
                	    },
                	    tooltip : {
                	        trigger: 'item',
                	        formatter: "{a} <br/>{b} : {c} ({d}%)"
                	    },
                	    legend: {
                	        orient : 'vertical',
                	        x : 'left',
                	        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                	    },
                	    toolbox: {
                	        show : true,
                	        feature : {
                	            mark : {show: true},
                	            dataView : {show: true, readOnly: false},
                	            restore : {show: true},
                	            saveAsImage : {show: true}
                	        }
                	    },
                	    calculable : true,
                	    series : [
                	        {
                	            name:'访问来源',
                	            type:'pie',
                	            radius : '55%',
                	            center: ['50%', '60%'],
                	            data:[
                	                {value:335, name:'直接访问'},
                	                {value:310, name:'邮件营销'},
                	                {value:234, name:'联盟广告'},
                	                {value:135, name:'视频广告'},
                	                {value:1548, name:'搜索引擎'}
                	            ]
                	        }
                	    ]
                	};
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
    </script>
	<div class="content">
			<div class="container">
				<h1 style="height:50px;margin:10px" align="center">统计信息-单属性特征描述</h1>
				<p align="center">门诊记录共有<?php echo $outpatient_log?>个属性，重要分析性属性共有<?php echo $outpatient_log_characters?>个</p>
				<div class="about-us">
					 <div class="about-header">
					    <h3>性别比例</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">当前科室门诊挂号记录中的男女人数比</a>
						<div id="s_a_d_gender" style="height:400px;width:600px">
						<?php
							include 'components/echart_diagrams.php';
							diagram_pie(
								"'s_a_d_gender'",
								"'性别比例'",
								"'男'，'女'",
								"{value:99,name:'男'},{value:103,name:'女'}");
						?>
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>年龄分布</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>病症分布</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>等待时间分布</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>看病人数趋势</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>医生接诊数量</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>

				<div class="about-us">
					 <div class="about-header">
					    <h3>复诊率与非复诊确诊率</h3>
					 </div>
					<div class="about-info">
						<a href="record_scheme.php">描述</a>
						内容
					</div>
				</div>
			</div>
			
		</div>
		<!-- footer -->
	<div class="footer">
		<div class="container">
			<p>Copyright &copy; 2015.Company name All rights reserved.<a target="_blank" href="http://sc.chinaz.com/moban/">&#x7F51;&#x9875;&#x6A21;&#x677F;</a></p>
		</div>
	</div>
</body>