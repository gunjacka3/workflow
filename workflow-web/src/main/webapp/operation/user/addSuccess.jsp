<%@ page contentType="text/html; charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv= "Refresh" content= "0;url=list.jsp "> -->
<title>jQuery EasyUI</title>
</head>
<body>

<style type="text/css">
#apDiv1 {
 	line-height:250px; 
    text-align:center;   
}
#apDiv2 {
	position:absolute;
	top:50px;
    text-align:center;   
}
</style>
<% 
		if(request.getAttribute("msg")!=null){
	%>
		<div id = "apDiv2" >
			用户已添加<br>
		<div style="color:#F00">※	密码设置失败，请联系管理员重置密码</div>
		</div>
		
	<% 
		}else{
	%>
		<div id = "apDiv1" class="dialog-box" >
			<span class="d-span" ><label>添加成功！</label></span>
		</div>
	<%	
		}
	%>
</body>
</html>