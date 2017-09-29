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
	<!-- <h1 class="logo">
	<img src="<%=basePath %>/images/u-m-logo.png" /> 
	</h1>-->
	<c:url var="loginUrl" value="/login"/>
	<form method="post" action="${pageContext.request.contextPath}/j_spring_security_check" >
	<div class="u-form">
		<p>用户名</p>
		<input   name="j_username" type="text" />
	</div>
	<div class="u-form">
		<p>用户密码</p>
		<input  	name="j_password" type="password" />
	</div>
	
	<c:if test="${not empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
         <div class="u-form" align="right" class="msg"><font color="red">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</font></div>
    </c:if>
    	
	<input type="submit" value="登录" class="s-btn">
	</form>
	<a href="<%=basePath %>/operation/user/update.jsp"><input type="submit" value="修改密码" class="s-btn"></a>
</div>	
<script type="text/javascript" src="js/jquery.min.js"></script>
<script>
$(document).ready(function(){
	$(".u-form").on("click","span",function(e){
		e.stopPropagation();
		$(this).parent().toggleClass("cur");
		var _this=$(this);
		_this.next(".u-list").on("click","li",function(){
			var val=$(this).text();
			_this.text(val);
			$(".u-list").parent().removeClass("cur");
			var hd=$(this).parents(".u-form").find("input");
			hd.val(val);		
		})
	})
	var html_click_btrue = true;
    $("html").on("click", function(){
        if(html_click_btrue)
            $(".u-list").parent().removeClass("cur");        
    });        
})
</script>
</body>
</html>