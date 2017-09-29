<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="author" content="有钱 - www.umoney.com" />
<meta name="copyright" content="有钱 - www.umoney.com" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<link rel="stylesheet" href="<%=basePath %>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath %>/css/login.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<link rel="stylesheet" href="<%=basePath %>/css/default.css" type="text/css" />
<script src="<%=basePath %>/js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/js/lhgdialog.js" type="text/javascript"></script>

<title>工作流平台</title>
</head>
<body>	
<div class="login">
	<h1 class="logo"><img src="<%=basePath %>/images/u-m-logo.png" /></h1>
	<% 
		String msg=null;
		if(request.getAttribute("msg")!=null){
			msg=request.getAttribute("msg").toString();
	%>
		<div style="color:#F00;font-size:15px">※<%=msg%></div>
	<% 
		}
	%>
	<form action="<%=basePath %>/updateUser.action" method="post">
	<div class="u-form">
		<p>用户名</p>
		<input name="id" type="text" />
	</div>
	<div class="u-form">
		<p>原密码</p>
		<input name="oldPassword" type="password" />
	</div>
	<div class="u-form">
		<p>新密码</p>
		<input	name="newPassword" type="password" />
	</div>
	<!-- <input type="submit" value ="修改" class="um-btn mr15 mt15 mb15 fr"/>
	<input type="reset" value = "重置" class="um-btn mr15 mt15 mb15 fr" /> -->
	<input type="submit" value="修改" class="s-btn">
	</form>
	<a href="<%=basePath %>/pages/login/loginpage.jsp"><input type="submit" value="取消" class="s-btn"></a>
</div>	

</body>
</html>