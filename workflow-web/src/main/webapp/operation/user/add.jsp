<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ page contentType="text/html; charset=utf-8" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String userId="";
	String password="";
	String familyName="";
	String givenName="";
	String mail="";
	
	if(request.getAttribute("userId")!=null){
		userId=request.getAttribute("userId").toString();
	}
	if(request.getAttribute("password")!=null){
		password=request.getAttribute("password").toString();
	}
	if(request.getAttribute("familyName")!=null){
		familyName=request.getAttribute("familyName").toString();
	}
	if(request.getAttribute("givenName")!=null){
		givenName=request.getAttribute("givenName").toString();
	}
	if(request.getAttribute("mail")!=null){
		mail=request.getAttribute("mail").toString();
	}
	
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<title>jQuery EasyUI</title>
</head>
<body>
	<% 
		String msg=null;
		if(request.getAttribute("msg")!=null){
		msg=request.getAttribute("msg").toString();
	%>
		<script >
				alert('<%=msg%>');
		</script>
	<% 
		}
	%>
 	<form action="<%=basePath %>/addUser.action" onsubmit="return isOr()" method="post">
	<input type="hidden" value="" name="id"/>
	<input type="hidden" id="btn" />
	<div class="dialog-box">
		<span class="d-span"><label>ID:</label><input type="text" value="<%=userId %>" id="userId" name="user.userId" class="um-input"/></span>
		<span class="d-span"><label>密码:</label><input type="password" value="<%=password %>" id="passWord" name="user.password" class="um-input"/></span>
		<span class="d-span"><label>姓:</label><input type="text" value="<%=familyName %>" id="familyName" name="user.familyName" class="um-input"/></span>
		<span class="d-span"><label>名:</label><input type="text" value="<%=givenName %>" id="givenName" name="user.givenName" class="um-input"/></span>
		<span class="d-span"><label>E-mail:</label><input type="text" id="email" onBlur="isMeeting()" value="<%=mail %>" name="user.mail" class="um-input"/></span>
		
		<input type="submit" value ="提交" class="um-btn mr15 mt15 mb15 fr"/>
		<input type="reset" value = "重置" class="um-btn mr15 mt15 mb15 fr" /></th>
	</div>
	</form>
</body>
<script type="text/javascript">
	function isOr(){
		var u= document.getElementById("userId").value;
		var p= document.getElementById("passWord").value;
		var f= document.getElementById("familyName").value;
		var g= document.getElementById("givenName").value;
		var e= document.getElementById("email").value;
		
		if(u!=null && u!=""){
			if(/[\u4E00-\u9FA5]/g.test(u)){
				alert("用户ID不能为中文");
				return false;
			}else{
				if(p!=null && p!=""){
					if(f!=null && f!=""){
						if(g!=null && g!=""){
							if(e!=null && e!=""){
								if(/^\w+@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(e)){    /*正则表达式，判断所填信邮箱是否正确*/  
	        						return true;     
	    						}else{
	    							alert("请输入有效的邮箱格式！");
	    							return false;
	    						}
							}else{
								alert("请输入邮箱地址");
								return false;
							}
						}else{
							alert("请输入名字");
							return false;
						}
					}else{
						alert("请输入姓氏");
						return false;
					}
				}else{
					alert("请输入密码");
					return false;
				}
			}
		}else{
			alert("请输入用户ID");
			return false;
		}
		
	}
</script>
</html>
					
				