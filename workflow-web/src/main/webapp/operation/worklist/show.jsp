<%@ page  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="cn.com.workflow.common.vo.ActiveNode"%>
<%@page import="java.util.List"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/temp_table.css">
	<link rel="stylesheet" href="<%=basePath%>css/default.css" type="text/css" />
	<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/lhgdialog.js" type="text/javascript"></script>
  </head>
	<body>
	<script type="text/javascript">
	$(document).ready(function() {
		$("div").click(function() {
					var titls = $(this).attr("title");
					var id = $(this).attr("id");
					if(id=="actity"){
						var dd=$("#"+titls+"_service");
						if(null!=dd){
							dd.toggle(1000);
						}
					}
				});
	});
	
	
	

	</script>
		<div>
			<img src="<%=basePath%>task/readResource/${taskId}.do" style="position:absolute; left:0px;top:0px;">
			
			<%
			List list1 = (List) request
						.getAttribute("actitys");
			List list2 = (List) request
					.getAttribute("actitysHis");
			List list3 = (List) request
					.getAttribute("actitysAll");
			
			
			if(list1 != null && list1.size()> 0){
				for(int i =0;i<list1.size();i++){
					ActiveNode pp = (ActiveNode)list1.get(i);
				%>
			
					<div title="<%=pp.getId()%>" id="actity" style="position:absolute; border:2px solid blue;left:<%=pp.getX()%>px;top:<%=pp.getY()%>px;width:<%=pp.getWidth()%>px;height:<%=pp.getHeight()%>px;"></div> 
					<% 	
			
					}
				}
			if(list2 != null && list2.size()> 0){
				for(int i =0;i<list2.size();i++){
					ActiveNode pp = (ActiveNode)list2.get(i);
				%>
		
			<div title="<%=pp.getId()%>" id="actity" style="filter: alpha(opacity=5); opacity:0.05;position:absolute; border:2px solid #FFFFFF ;left:<%=pp.getX()%>px;top:<%=pp.getY()%>px;width:<%=pp.getWidth()%>px;height:<%=pp.getHeight()%>px;"></div>
			<%
				}
			}
			if(list2 != null && list2.size()> 0){
				for(int i =0;i<list2.size();i++){
					ActiveNode pp = (ActiveNode)list2.get(i);
						List list4 = pp.getActiveNodes();
						if(list4 != null && list4.size()> 0){
							
						%>
					<div style="display:none;background:#FFFFFF; filter: alpha(opacity=100); opacity:1.0; position:absolute; border:2px solid #77DDFF ;left:<%=pp.getParentX()%>px;top:<%=pp.getParentY()%>px;width:<%=pp.getParentW()%>px;height:<%=pp.getParentH()%>px;" id="<%=pp.getParentId()%>">
						<%
							for(int j =0;j<list4.size();j++){
								ActiveNode an = (ActiveNode)list4.get(j);
									if(an.isEnableFlag()){
					%>
					<div title="<%=an.getId()%>" id="actity" style="vertical-align:middle; font-weight:bold; text-align:center;background:#FFFFFF; filter: alpha(opacity=100); opacity:1.0; position:inherit; border:2px solid #B22222 ;left:<%=an.getX()%>px;top:<%=an.getY()%>px;width:<%=an.getWidth()%>px;height:<%=an.getHeight()%>px;"><%=an.getName()%></div> 
							<% 	
									}else{
										%>
					<div title="<%=an.getId()%>" id="actity" style="vertical-align:middle; font-weight:bold; text-align:center;background:#FFFFFF; filter: alpha(opacity=100); opacity:1.0; position:inherit; border:2px solid #3CB371 ;left:<%=an.getX()%>px;top:<%=an.getY()%>px;width:<%=an.getWidth()%>px;height:<%=an.getHeight()%>px;"><%=an.getName()%></div>					
										<% 
									}
							}
					%>
					</div>
					<% 	
						}
					}
				}
			if(list3 != null && list3.size()> 0){
				for(int i =0;i<list3.size();i++){
					ActiveNode pp = (ActiveNode)list3.get(i);
				%>
		
			<div title="<%=pp.getId()%>" id="actity" style="position:absolute; border:2px solid #FFDC35 ;left:<%=pp.getX()%>px;top:<%=pp.getY()%>px;width:<%=pp.getWidth()%>px;height:<%=pp.getHeight()%>px;"></div> 
					<% 	
			
					}
				}
			%>
			
		</div>


	</body>
</html>
