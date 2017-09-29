<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@page
	import="cn.com.workflow.common.vo.ActDefinitionVO"%>
<%@page
	import="cn.com.workflow.common.vo.ProcessDefinitionVO"%>
<%@page import="cn.com.workflow.common.Page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<link rel="stylesheet" href="<%=basePath%>css/default.css"
	type="text/css" />
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/lhgdialog.js" type="text/javascript"></script>
</head>
<body>
	<script type="text/javascript">
		i = 1;
		$(document)
				.ready(
						function() {

							$("select")
									.change(
											function() {
												var strs = $(this).children(
														'option:selected')
														.val().split(",");

												if (strs[0] == "remove") {
													$
															.ajax({
																url : "hisProcess/"+strs[0]+ ".do",
																data : strs[1],
																type : "POST",
																async : false,
																dataType : "text",
																contentType : "text/plain",
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																	alert(XMLHttpRequest.status);
																	alert(XMLHttpRequest.readyState);
																	alert(textStatus);
																},
																success : function(
																		json) {
																	location.href = "hisProcess/initList.do";
																}
															})
												}else if(strs[0] =="pic"){
													$.dialog({
														title:'流程图',
														modal:true,
														lock: true,
														height:400, 
														width:1250,
														background: '#000', /* 背景色 */
														opacity: 0.5,       /* 透明度 */
														content:"url:<%=basePath%>diagram-viewer/index.html?processDefinitionId="+strs[2]+"&processInstanceId="+strs[1],
														buttons: {
													        "Ok": function() {
													          $(this).dialog("close");
													        }
													      }
														
													});
												
												}
											});
							
								$('#clsButton').click(function(){
									$('#templateName').val('');
									location.href = "hisProcess/initDefinition.do";
								});

						});

		function del_1(o) {
			document.getElementById("newUpload1").removeChild(
					document.getElementById("div_" + o));
		}
		
		function millisecondToDate(msd) {
		    var time = parseFloat(msd) / 1000;
		    if (null != time && "" != time) {
		        if (time > 60 && time < 60 * 60) {
		            time = parseInt(time / 60.0) + "分钟" + parseInt((parseFloat(time / 60.0) -
		                parseInt(time / 60.0)) * 60) + "秒";
		        }
		        else if (time >= 60 * 60 && time < 60 * 60 * 24) {
		            time = parseInt(time / 3600.0) + "小时" + parseInt((parseFloat(time / 3600.0) -
		                parseInt(time / 3600.0)) * 60) + "分钟" +
		                parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
		                parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
		        }
		        else {
		            time = parseInt(time) + "秒";
		        }
		    }
		    return time;
		}
	</script>
	
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table>
				<tr>
				<td>
				<div id="table-wrapper" title="上传模型" class="template">
					<form action="hisProcess/showList.do" method="get">
						<table>
							<tr>
								<td>模板名称：</td>
								<td><input type="text" name="templateName" id="templateName"
									value="${templateName}" /></td>
								<td><input type="submit" value="查询" class="um-btn" /></td>
								<td><input id="clsButton" type="button"  value="reset" class="um-btn"></td>
							</tr>
						</table>
					</form>
				</div>
				</td>
				</tr>
		</table>
	</div>		
	<hr>
	<div class="table-wrapper">
		<table id="um-table-reload" class="um-table">
			<thead>
				<tr>
					<th>序号</th>
					<th>id值</th>
					<th>流程名称</th>
					<th>业务主键</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>处理时间 </th>
					<th>发起用户 </th>
					<th>是否撤销</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${templates.actHisProcessListVOs}" var="actdf"
					varStatus="status">
					<tr>
						<td>${status.index + 1}</td>
						<td>${actdf.id}</td>
						<td>${actdf.name}</td>
						<td>${actdf.businessKey}</td>
						<td>${actdf.startTime}</td>
						<td>${actdf.endTime}</td>
						<td>
						${actdf.durationInMillis}
						</td>
						<td>${actdf.startUserId}</td>
						<td>
							<c:choose>
								<c:when test="${empty actdf.deleteReason}">
									否
								</c:when>
								<c:otherwise>
									"${actdf.deleteReason}
								</c:otherwise>
							</c:choose>
						</td>
						<td><select id="sel">
								<option value="">--操作--</option>
								<option value="detils,${actdf.id}">详情</option>
								<option value="pic,${actdf.id},${actdf.processDefinitionId}">流程图</option>
						</select></td>
					</tr>
				</c:forEach>
				<tr>
					<div class="um-table-page fr">
						 <a
							href="hisProcess/showList.do?pageNo=1&templateName=${templateName}">首页</a>
						<c:choose>
							<c:when test="${page.pageNo - 1 > 0}">
								<a
									href="hisProcess/showList.do?pageNo=${page.pageNo - 1}&templateName=${templateName}">上一页</a>
							</c:when>
							<c:when test="${page.pageNo - 1 <= 0}">
								<a href="hisProcess/showList.do?pageNo=1&templateName=${templateName}">上一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="hisProcess/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 < page.totalPage}">
								<a
									href="hisProcess/showList.do?pageNo=${page.pageNo + 1}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 >= page.totalPage}">
								<a
									href="hisProcess/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">下一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="hisProcess/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">尾页</a>
							</c:when>
							<c:otherwise>
								<a
									href="hisProcess/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">尾页</a>
							</c:otherwise>
						</c:choose>
						<font size="2">共 ${page.totalPage}页 </font> <font size="2">第
							${page.pageNo} 页</font>
					</div>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
