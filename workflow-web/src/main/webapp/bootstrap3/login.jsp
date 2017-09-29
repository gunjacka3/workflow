<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>登录</title>
    <link href="<%=basePath %>css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=basePath %>css/login2.css" rel="stylesheet"  type="text/css" />
    <script src="<%=basePath %>js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=basePath %>js/bootstrap.min.js" type="text/javascript"></script>
     <script src="<%=basePath %>js/jquery.validate.min.js" type="text/javascript"></script>
  </head>
  
  <body class="background-color: #fff;">
    <div class="login">
			<c:url var="loginUrl" value="/login"/>
			<form method="post" action="${pageContext.request.contextPath}/j_spring_security_check" class="form-horizontal">
			<div class="form-group">
				<label for="inputName" class="col-sm-2 control-label">User</label>
				 <div class="col-sm-10">
					<input name="j_username" type="text" class="form-control" id="inputName" placeholder="用户名称" required>
				 </div>
			</div>
			<br>
			<div class="form-group">
				<label for="inputPassword" class="col-sm-2 control-label">Password</label>
				 <div class="col-sm-10">
				<input name="j_password" type="password" class="form-control" id="inputPassword" placeholder="密码" required>
				</div>
			</div>
			
			<c:if test="${not empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
		         <div class="form-group" align="right" class="msg">
		         	<font color="red">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</font>
		         </div>
		    </c:if>
			<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-default">登  入</button>
		      <a href="<%=basePath %>operation/user/update.jsp"><button type="submit"  class="btn btn-default">修改密码</button></a>
		    </div>
	    
  </div>
	</form>
</div>	
  </body>
</html>