<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.com.workflow.user.Users"%>
<%@page import="cn.com.workflow.common.vo.OSInfoVO"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page language="java" pageEncoding="UTF-8"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Users user = (Users)request.getSession().getAttribute("user");
	OSInfoVO osInfo = (OSInfoVO)request.getSession().getAttribute("osInfo");
  	String usercd = user.getUserCd();
  	String userLevel = user.getUserLevel();
  	String orgcd = user.getOrgCd();
  %>
<!DOCTYPE html>
    <html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>AgileUI</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <!--[if lt IE 9]>
          <script src="assets/js/minified/core/html5shiv.min.js"></script>
          <script src="assets/js/minified/core/respond.min.js"></script>
        <![endif]-->
        <!-- AgileUI CSS Core -->
        
        <link rel="stylesheet" type="text/css" href="<%=basePath %>pages/css/minified/aui-production.min.css">
        <link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" type="text/css" >
        <!-- Theme UI -->
        <link id="layout-theme" rel="stylesheet" type="text/css" href="<%=basePath %>pages/themes/agileui/color-schemes/layouts/default.css">
        <link id="elements-theme" rel="stylesheet" type="text/css" href="<%=basePath %>pages/themes/minified/agileui/color-schemes/elements/default.min.css">
        <!-- AgileUI Responsive -->
        <link rel="stylesheet" type="text/css" href="<%=basePath %>pages/themes/agileui/responsive.css">
        <!-- AgileUI Animations -->
        <link rel="stylesheet" type="text/css" href="<%=basePath %>pages/themes/minified/agileui/animations.min.css">
        <link rel="stylesheet" href="<%=basePath %>pages/css/bootstrap-addtabs.css" type="text/css" media="screen" />
        
        <!-- AgileUI JS -->
        <script src="<%=basePath%>pages/js/jquery.js" type="text/javascript"></script>
        
         <script type="text/javascript" src="<%=basePath %>pages/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=basePath %>pages/js/bootstrap-addtabs.js"></script>
        <script type="text/javascript" src="<%=basePath %>pages/js/wzy.js"></script>
        
        <script type="text/javascript">
        $(document).ready(
				function() {
					$('#tabs a').click(function (e) {
		        		  e.preventDefault()
		        		  $(this).tab('show')
		        		});
				});
        </script> 
    </head>
    <body class="fixed-sidebar fixed-header">
        <div id="loading" class="ui-front loader ui-widget-overlay bg-white opacity-100">
            <img src="<%=basePath %>pages/images/loader-dark.gif" alt="">
        </div>
        <div id="page-wrapper" class="demo-example">

            <div id="page-sidebar">
                <div id="header-logo">
                  	  工作流平台 <i class="opacity-80">v0.1</i>
                    <a href="javascript:;" class="tooltip-button" data-placement="bottom" title="Close sidebar" id="close-sidebar">
                        <i class="glyph-icon icon-align-justify"></i>
                    </a>
                    <a href="javascript:;" class="tooltip-button hidden" data-placement="bottom" title="Open sidebar" id="rm-close-sidebar">
                        <i class="glyph-icon icon-align-justify"></i>
                    </a>
                    <a href="javascript:;" class="tooltip-button hidden" title="Navigation Menu" id="responsive-open-menu">
                        <i class="glyph-icon icon-align-justify"></i>
                    </a>
                </div>
                <div id="sidebar-search">
                </div>
                <div id="sidebar-menu" class="scrollable-content">
                    <ul>
   <!--                 
 						<li>
                            <a href="index.html" title="Dashboard">
                                <i class="glyph-icon icon-dashboard"></i>
                              		  工作台
                            </a>
                        </li>
      -->                   
     					<li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-dashboard"></i>
                                	模板管理
                            </a>
                            <ul>
                                <li>
                                    <a id="model" href="<%=basePath %>model/initDefinition.do" title="模板设计" >
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	 模板设计
                                    </a>
                                </li>
                                <li>
                                    <a id="prodef" href="<%=basePath %>proDef/initDefinition.do" title="已部署模型" >
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	已部署模型	
                                    </a>
                                </li>
                            </ul>
                        </li>
						<li>
                            <a href="javascript:;" title="">
                                <i class="glyph-icon icon-dashboard"></i>
                                	个人任务管理
                            </a>
                            <ul>
                                <li>
                                    <a id="candi" href="<%=basePath %>worklist/initCandiWorkList.do" title="待分配任务" data-toggle="tab">
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	 待分配任务
                                    </a>
                                </li>
                                <li>
                                    <a id="todo" href="<%=basePath %>worklist/initWorkList.do" title="待办任务">
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	待办任务	
                                    </a>
                                </li>
                                <li>
                                    <a id="his" href="<%=basePath %>hisProcess/initList.do" title="历史任务">
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	历史任务	
                                    </a>
                                </li>
                                <li>
                                    <a id="suspend" href="<%=basePath %>worklist/initSuspendList.do" title="挂起任务">
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	挂起任务	
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li>
                           	<a href="javascript:;" title="">
                                <i class="glyph-icon icon-dashboard"></i>
                                	统计分析
                            </a>
 							<ul>
                                <li>
                                    <a id="candi" href="<%=basePath %>statistics/findProcessInstanceCount.do" title="待分配任务" data-toggle="tab">
                                        <i class="glyph-icon icon-chevron-right"></i>
                                       	 流程实例
                                    </a>
                                </li>
                            </ul>
                            
                        </li>
                    </ul>
                    <div class="divider mrg5T mobile-hidden"></div>
                    <div class="text-center mobile-hidden">
                    </div>
                </div>

            </div><!-- #page-sidebar -->
            
            <div id="page-main">

                <div id="page-main-wrapper">

                    <div id="page-header" class="clearfix">
                        <div id="page-header-wrapper" class="clearfix">
                            <div class="top-icon-bar dropdown">
                                <a href="javascript:;" title="" class="user-ico clearfix" data-toggle="dropdown">
                                    <img width="36" src="<%=basePath %>pages/images/user.png" alt="">
                                    <span>&nbsp;<%=usercd%>&nbsp;</span>
                                    <i class="glyph-icon icon-chevron-down"></i>
                                </a>
                                <ul class="dropdown-menu float-right">
                                    <li>
                                        <a class="font-orange" href="<%=basePath %>logout" title="">
                                            <i class="glyph-icon icon-cog mrg5R"></i>
                                            Logout
                                        </a>
                                    </li>
                                    <li class="divider"></li>
                                    <li>
                                        <a href="javascript:;" title="">
                                            Another menu item
                                        </a>
                                    </li>

                                </ul>
                            </div>
                            <div class="top-icon-bar">
                            </div>
                        </div>
                    </div><!-- #page-header -->
 					<div id="page-breadcrumb-wrapper">
                        <div id="page-breadcrumb">
                            <a href="javascript:;" title="Dashboard">
                                <i class="glyph-icon icon-dashboard"></i>
                                Dashboard
                            </a>
                            <a href="javascript:;" title="Elements">
                                <i class="glyph-icon icon-laptop"></i>
                                Elements
                            </a>
                            <span class="current">
                                Example breadcrumb
                            </span>
                        </div>
                    </div>
                    
                    <!-- #page-breadcrumb-wrapper -->
                    
                    <div id="page-content">
                        <div id="tabs" class="tabs">
		                        <ul  class="nav nav-tabs"  role="tablist">
		                         <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Home</a></li>   
		                        </ul>
		                    </h3>
	                    <div class="tab-content">
                            <div role="tabpanel" class="tab-pane active" id="home">
                            	<div class="well well-lg">
									<table id="sysinfos_table" class="table">
									<tbody>
										<tr>
											<td>主机名称</td>
											<td><%=osInfo.getHostName()%></td>
											<td>IP地址</td>
											<td><%=osInfo.getIpAddress()%></td>
										</tr>
										<tr>
											<td>操作系统</td>
											<td><%=osInfo.getOsName()%></td>
											<td>系统架构</td>
											<td><%=osInfo.getOsArch()%></td>		
										</tr>
										<tr>
											<td>系统版本</td>
											<td><%=osInfo.getOsVersion()%></td>
											<td>java版本</td>
											<td><%=osInfo.getJavaVersion()%></td>		
										</tr>
										<tr>
											<td>java来源</td>
											<td><%=osInfo.getJavaVendor()%></td>
											<td>机器名</td>
											<td><%=osInfo.getComputerName()%></td>		
										</tr>
										<tr>
											<td>当前内存</td>
											<td><%=osInfo.getTotalMemory()%></td>
											<td>空闲内存</td>
											<td><%=osInfo.getFreeMemory()%></td>		
										</tr>
										<tr>
											<td>处理器数量</td>
											<td><%=osInfo.getAvailableProcessors()%></td>
											<td>web容器名称</td>
											<td><%=osInfo.getServerName()%></td>		
										</tr>
										</tbody>
									</table>
									</div>
                			</div>  
                		</div>
                </div>
                    </div>
	            </div><!-- #page-main -->
            </div><!-- #page-main-wrapper -->
        </div><!-- #page-wrapper -->

    </body>
</html>

