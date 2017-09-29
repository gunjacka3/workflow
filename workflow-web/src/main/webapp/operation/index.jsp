<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.workflow.redis.impl.RedisServiceImpl"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.com.workflow.user.Users"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page language="java" pageEncoding="UTF-8"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//System.out.println("=basePath===="+basePath);
%>
<%
	Users user = (Users)request.getSession().getAttribute("user");
  	String usercd = user.getUserCd();
  	String userLevel = user.getUserLevel();
  	String orgcd = user.getOrgCd();
  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta name="author" content="有钱 - www.umoney.baidu.com" />
	<meta name="copyright" content="有钱 - www.umoney.baidu.com" />
	<meta name="description" content="" />
	<meta name="keywords" content="" />
	<link rel="stylesheet" href="<%=basePath %>css/reset.css" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/um-page.css">
	<link rel="stylesheet" href="<%=basePath %>css/default.css" type="text/css" />
	<link rel="stylesheet" href="<%=basePath %>css/jquery.ui.css" type="text/css" />
	<link rel="stylesheet" href="<%=basePath %>css/index.css" type="text/css" />
	<script src="<%=basePath %>js/jquery.min.js" type="text/javascript"></script>
	<script src="<%=basePath %>js/lhgdialog.js" type="text/javascript"></script>
<title>工作流平台</title>
<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.layout.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath %>js/navmenu.js"></script>
<script type="text/javascript" src="<%=basePath %>js/sphinx.js"></script>
<script type="text/javascript">
		$(document).ready(function(){
			var layout = $('body').layout({
				defaults : {
					applyDefaultStyles: false,
					slidable : false,
					resizable : false,
					closable : false,
					spacing_open : 0,
					spacing_closed : 0
				},
				north : {
					spacing_open : 7, 
					size : 110
				},
				west : {
					size : 175,
					spacing_open : 7,    
					spacing_closed : 7,
					closable : true,
					togglerTip_open : "关闭菜单",
					togglerTip_closed : "打开菜单"
				},
				center : {
					onresize_end : function(name,element,state) {
						var newHeight = state.innerHeight - 60;
						if(tabs) {
							tabs.resize(newHeight);
						}
					}
				}
			});
			//初始化Tab
			var tabs = new DynamicTab($('#tabs'),{wrapperHeight:getTabWrapperHeight(layout)});

			/*监听左侧菜单点击事件*/
			$("ul.sidebar-menu").navmenu({onClick: function(event, id, code, text, href) {
				event.preventDefault();
				var tabId = 'tab_' + code;
				var title = text;
				var url = href;
				if(url && url.substring(0,1) != '#') {
					url = '${ctx}' + url;
					tabs.addTab(tabId, title, url);
				} else {
					tabs.addStaticTab(tabId, title, '<span>功能开发中，敬请期待！</span>');
				}
			}});
			
			$(".nav ul").on("click","li",function(){
				var siderUl=$(".sidebar>ul");
				$(".sidebar>ul").eq($(this).index()).show().siblings().hide();
			})
			
		});
		function getTabWrapperHeight(layout) {
			var state = layout.state;
			var contentHeight = state.center.innerHeight;
			return contentHeight - 60;
		};
</script>
<script type="text/javascript">
		function refresh(){  
		 		if(confirm("确定重新加载数据吗？")){
					window.location.href='refresh.action?usercd=<%=usercd%>&userLevel=<%=userLevel%>&orgcd=<%=orgcd%>';
				}
		  }
				
		
</script> 
</head>
<body>
<div class="ui-layout-center">
		<div id="tabs">
			<ul>
				<li><a href="#home">首页</a></li>
			</ul>
			<div id="home">
				<img  style="opacity:0.3;filter:alpha(opacity=30);right:0px;height:auto; line-height:20px; padding-top:400px;position:absolute;bottom:1%" src="<%=basePath %>images/activiti.png" >
			</div>
		</div>
		
	</div>
	<div class="ui-layout-west">
		<div class="sidebar-header">
			<span class="icon customer"></span>
		</div>
		<div class="sidebar">
			<ul class="sidebar-menu">
	            <li >
	                <a href="#">
	                   个人任务管理
	                </a>
					<ul class="list" style="display:block">
						<li id="wdksha" resid="1">
							<a href="<%=basePath %>worklist/initCandiWorkList.do">待分配任务</a>
						</li>
						<li id="wdklist" resid="2">
							<a href="<%=basePath %>worklist/initWorkList.do">待办任务</a>
						</li>
						<li id="hislist" resid="3">
							<a href="<%=basePath %>hisProcess/initList.do">历史任务</a>
						</li>
						<li id="wdksus" resid="4">
							<a href="<%=basePath %>worklist/initSuspendList.do">挂起任务</a>
						</li>
						
					</ul>
	            </li>
	            <li >
	                <a href="#">
	                   他人任务管理
	                </a>
					<ul class="list" style="display:block">
						<li id="wdkaa" resid="1">
							<a href="<%=basePath%>/jbpmservice/others/shareList.jsp">待分配任务列表</a>
						</li>
						<li id="wdkbb" resid="2">
							<a href="<%=basePath%>/jbpmservice/others/list.jsp">待办任务列表</a>
						</li>
						<li id="wdkbc" resid="3">
							<a href="<%=basePath%>/jbpmservice/others/joinList.jsp">会签任务待办列表</a>
						</li>
						<li id="wdkcd" resid="4">
							<a href="<%=basePath%>/jbpmservice/others/suspendList.jsp">挂起任务列表</a>
						</li>
						<li id="wdkde" resid="5">
							<a href="<%=basePath%>/jbpmservice/others/HisList.jsp">历史列表</a>
						</li>
						
					</ul>
	            </li>
	            <li >
	                <a href="#">
	                   历史任务查询
	                </a>
					<ul class="list" style="display:block">
						<li id="wdkHisAll" resid="4">
							<a href="hisListAll.action?msg=allHis">查询所有历史信息</a>
							<!-- 参数msg用来区分查询所有历史还是查询个人历史，无其他含义-->
						</li>
					</ul>
	            </li>
			</ul>
			<ul style="display:none" class="sidebar-menu">
				<li >
	                <a href="#">
	                   流程配置
	                </a>
					<ul class="list" style="display:block"
					>
						<li id="lcmx" resid="1">
							<a href="<%=basePath %>model/initDefinition.do">模板设计</a>
						</li>
						<li id="bsmx" resid="2">
							<a href="<%=basePath %>proDef/initDefinition.do">已部署模型</a>
						</li>
					</ul>
	            </li>
	           <li id="xnkh" resid="2">
	                <a href="#">
	                    用户&分组
	                </a>
	                <ul class="list" style="display:block">
						<li id="wdkc" resid="1">
							<a href="<%=basePath %>/userList.action">用户列表</a>
						</li>
						<li id="wdkd" resid="2">
							<a href="<%=basePath %>/groupList.action">分组列表</a>
						</li>
					</ul>
	            </li>
	            <li >
	                <a href="#">
	                   定时任务管理
	                </a>
					<ul class="list" style="display:block"
					>
						<li id="wdke" resid="1">
							<a href="<%=basePath %>/planjob.action">计划中任务</a>
						</li>
						<li id="wdkf" resid="2">
							<a href="<%=basePath %>/executiveList.action">执行中任务</a>
						</li>
						
					</ul>
	            </li> 
			</ul>
		</div>
	</div>
	
	<div class="ui-layout-north">
		<div class="header">
			<div class="clearfix">
				<!-- <div class="logo fl"><img src="<%=basePath %>images/um-logo.png"></div> -->
				<div class="title fl">&nbsp;工作流平台</div>
 				
				<div class="list fr">
					<p>
						<span>当前用户：<em><%=usercd%></em></span>
					</p>
					<c:url value="/logout" var="logoutUrl"/>
					<p class="exit"><a href="${logoutUrl}">登出</a>
					</p>
				</div>
			</div>
			<div class="nav clearfix">
				<ul>
					<li id="home"><a href="#">首页</a></li>
					<li id="mbgl"><a href="#">配置管理</a></li>
					<li id=""><a href="JavaScript:refresh()">重新加载</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="ui-layout-south">
		<div class="footer">工作流平台 &copy 2015</div>
	</div>

</body>
</html>
