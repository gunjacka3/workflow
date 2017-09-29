<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@page import="cn.com.workflow.common.Page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<link rel="stylesheet" href="<%=basePath %>css/reset.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath%>css/default.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>css/bootstrap-dialog.min.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/um-page2.css">
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/jquery-ui.js"></script>
<script src="<%=basePath%>js/bootstrap-dialog.min.js"></script>
<script src="<%=basePath %>js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath %>pages/js/wzy.js"></script>
</head>
<body>
	<script type="text/javascript">
	$(document)
	.ready(
			function() {
				$('a').click(function() {
							var strs = $(this).attr('name').split(",");

												if (strs[0] == "handle") {
													BootstrapDialog.show({
														type:BootstrapDialog.TYPE_INFO,
														title:"任务提交",
														cssClass: 'login-dialog',
											            message: function(dialog) {
											                var $message = $('<div></div>');
											                var pageToLoad = dialog.getData('pageToLoad');
											                $message.load(pageToLoad);
											                return $message;
											            },
											            data: {
											                'pageToLoad': '<%=basePath%>task/handle/'+strs[1]+ '.do'
											            },
											            buttons: [{
											            	icon: 'glyphicon glyphicon-ok',
											                label: '确定',
											                cssClass:'btn-warning',
											                hotkey:13,
											                draggable: true,
											                action: function(dialog){
																if(""!=$('#submit-form').serialize()){
																	json_data=$('#submit-form').serialize();
																	json_data = decodeURIComponent(json_data,true);
																}
											                	$.ajax({
											                		url:"task/submit/"+strs[1]+".do",
											                		type:"POST",
											                		dataType:"text",
											                		data:json_data,
											                		contentType : "text/plain",
											                		success:function(data){
											                			if(data!=""){
												                			var d=JSON.parse(data);
												                			if(d.message !=""){
												                				BootstrapDialog.show({
												                		            title: '异常',
												                		            message: d.message
												                		        });
												                			}
											                			}
												                			dialog.close();
												                			location.href = "worklist/initWorkList.do";
											                		}
											                		
											                	})
											                }
											            },{
											            	icon: 'glyphicon glyphicon-ok',
											                label: '临时保存',
											                cssClass:'btn-warning',
											                hotkey:13,
											                draggable: true,
											                action: function(dialog){
																if(""!=$('#submit-form').serialize()){
																	json_data=$('#submit-form').serialize();
																	json_data = decodeURIComponent(json_data,true);
																}
											                	$.ajax({
											                		url:"task/save/"+strs[1]+".do",
											                		type:"POST",
											                		dataType:"text",
											                		data:json_data,
											                		contentType : "text/plain",
											                		success:function(data){
											                			if(data!=""){
												                			var d=JSON.parse(data);
												                			if(d.message !=""){
												                				BootstrapDialog.show({
												                		            title: '异常',
												                		            message:d.message
												                		        });
												                			}
											                			}
											                			dialog.close();
											                		}
											                		
											                	})
											                }
											            },{
											            	icon: 'glyphicon glyphicon-remove',
											                label: '取消',
											                cssClass:'btn-warning',
											                hotkey:13,
											                action: function(dialog){
											                	dialog.close();
											                }
											            }]
											        });
												
											}else if(strs[0] =="pic"){
												BootstrapDialog.show({
													type:BootstrapDialog.TYPE_INFO,
													title:"查看流程图",
													cssClass: 'wide-dialog',
										            message: function(dialog) {
										                var $message = $('<div style="height:300px; overflow:auto"></div>');
										                var pageToLoad = dialog.getData('pageToLoad');
										                $message.load(pageToLoad);
										        
										                return $message;
										            },
										            data: {
										                'pageToLoad': '<%=basePath%>diagram-viewer/index2.jsp?processDefinitionId='+strs[2]+'&processInstanceId='+strs[1]
										            },
										            buttons: [{
										            	icon: 'glyphicon glyphicon-remove',
										                label: '确认',
										                cssClass:'btn-warning',
										                hotkey:13,
										                action: function(dialog){
										                	dialog.close();
										                }
										            }]
										        });
											}else if(strs[0] =="replace"){
												BootstrapDialog.confirm({
										            title: '提示',
										            message: '确认拒签该任务吗?',
										            type: BootstrapDialog.TYPE_INFO,
										            closable: true, 
										            draggable: true, 
										            btnCancelLabel: '拒绝', 
										            btnOKLabel: '同意', 
										            btnOKClass: 'btn-warning', 
										            callback: function(result) {
										            	if(result) {
											            	$.ajax({
																url : "task/replace/"+strs[1]+ ".do",
																data : strs[1],
																type : "POST",
																async : false,
																dataType : "text",
																contentType : "text/plain",
																
																success : function(
																		json) {
																	if(json!=""){
											                			var d=JSON.parse(json);
											                			if(d.message !=""){
											                				BootstrapDialog.show({
											                		            title: '异常',
											                		            message: d.message
											                		        });
											                			}
										                			}else{
																      $.jGrowl("任务已领取", { 
																          sticky: false,
																          position: 'bottom-right',
																          theme: 'bg-red btn text-left'
																        });
																	location.href = "worklist/initWorkList.do";
										                			}
																}
															})
											            }else {
											                
											            }
										            }
										        });
											}else if(strs[0] =="turn"){
												BootstrapDialog.show({
													type:BootstrapDialog.TYPE_INFO,
													title:"选择被移交人",
													cssClass: 'taskList-dialog',
										            message: function(dialog) {
										                var $message = $('<div></div>');
										                var pageToLoad = dialog.getData('pageToLoad');
										                $message.load(pageToLoad);
										        
										                return $message;
										            },
										            data: {
										            	'pageToLoad': '<%=basePath%>task/turn/'+strs[1]+ '.do'
										            },
										            buttons: [{
										            	icon: 'glyphicon glyphicon-ok',
										                label: '取消',
										                cssClass:'btn-warning',
										                hotkey:13,
										                draggable: true,
										                action: function(dialog){
										                	dialog.close();
										                }
										            },{
										            	icon: 'glyphicon glyphicon-ok',
										                label: '确定',
										                cssClass:'btn-warning',
										                hotkey:13,
										                draggable: true,
										                action: function(dialog){
										                	if(""!=$('#turn-form').serialize()){
																json_data=$('#turn-form').serialize();
																json_data = decodeURIComponent(json_data,true);
															}
										                	$.ajax({
										                		url:"task/delegate/"+strs[1]+".do",
										                		type:"POST",
										                		dataType:"text",
										                		data:json_data,
										                		contentType : "text/plain",
										                		success:function(data){
										                			if(data!=""){
											                			var d=JSON.parse(data);
											                			if(d.message !=""){
											                				BootstrapDialog.show({
											                		            title: '异常',
											                		            message: d.message
											                		        });
											                			}
										                			}
											                			dialog.close();
											                			location.href = "worklist/initWorkList.do";
										                		}
										                		
										                	})
										                }
										            }]
										            
										        });
											}
												
										});
							
								$('#clsButton').click(function(){
									$('#customerName').val('');
									$('#bizType').val('');
									location.href = "worklist/initWorkList.do";
								});
								
								$('#findUser').click(function(){
									alert("select");
								});
								$("#btn1").click(function(){     
							          
								    $("[name='checkbox']").attr("checked",'true');//全选     
								       
								});  
								
								$("#btn5").click(function(){   
								    var str="";     
								    $("[name = checkbox]:checkbox").each(function () {
								    	if ($(this).attr('checked')) {
								    		if(str==""){
								    			str=$(this).val(); 
								    		}else{
								    			str+=","+$(this).val(); 
								    		}
								     		    
								    	}
								    })     
								    
								    BootstrapDialog.show({
										type:BootstrapDialog.TYPE_INFO,
										title:"任务提交",
										cssClass: 'login-dialog',
							            message: function(dialog) {
							                var $message = $('<div></div>');
							                var pageToLoad = dialog.getData('pageToLoad');
							                $message.load(pageToLoad);
							                return $message;
							            },
							            data: {
							                'pageToLoad': '<%=basePath%>task/handleBatch/'+str+ '.do'
							            },
							            buttons: [{
							            	icon: 'glyphicon glyphicon-ok',
							                label: '确定',
							                cssClass:'btn-warning',
							                hotkey:13,
							                draggable: true,
							                action: function(dialog){
												if(""!=$('#submit-form').serialize()){
													json_data=$('#submit-form').serialize();
													json_data = decodeURIComponent(json_data,true);
												}
							                	$.ajax({
							                		url:"task/batch/"+str+".do",
							                		type:"POST",
							                		dataType:"text",
							                		data:json_data,
							                		contentType : "text/plain",
							                		success:function(data){
							                			if(data!=""){
								                			var d=JSON.parse(data);
								                			if(d.message !=""){
								                				BootstrapDialog.show({
								                		            title: '异常',
								                		            message: d.message
								                		        });
								                			}
							                			}else{
							                				dialog.close();
							                				location.href = "worklist/initWorkList.do";
							                			}
								                			
								                			
							                		}
							                		
							                	})
							                }
							            },{
							            	icon: 'glyphicon glyphicon-remove',
							                label: '取消',
							                cssClass:'btn-warning',
							                hotkey:13,
							                action: function(dialog){
							                	dialog.close();
							                }
							            }]
							        });
								 
								});
								$("[name='checkbox']").click(function(){
									$(this).attr("checked", !$(this).attr("checked"));
								});

						});


	</script>
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">

		<table width="100%">
				<tr>
					<td width="30%"  style="text-align:left;">
						<div id="findtodo" title="查询列表" class="template" style="width: 100%; text-align: left;" >
							<form id="conditionForm" action="<%=basePath%>worklist/queryWorkList.do"   method="post">
							<table  class="table table-condensed">
								<tr>
									<td width="30%">
										<div class="input-group input-group-sm">
											<span class="input-group-addon" id="basic-addon1">客户名称</span>
											<input id="customerName"  name="customerName" aria-describedby="basic-addon1" class="form-control" type="text" value="${workListCondition.customerName}" placeholder="客户名称...">
										</div>
									</td>
									<td width="30%">
										<div class="input-group input-group-sm">
											<span class="input-group-addon" id="basic-addon2">业务类型</span>
											<input id="bizType"  name="bizType" aria-describedby="basic-addon2" class="form-control" type="text" value="${workListCondition.bizType}" placeholder="业务类型...">
										</div>
									</td>
									<td>
										<div class="btn-group btn-group-sm"  role="group" aria-label="...">
												<button class="btn btn-default" type="submit" >查询</button>
												<button class="btn btn-default" type="button" onclick="$('input[id=clsButton]').click();" >重置</button>
								 				<input id="clsButton" type="button"  value="reset" style="display:none">
										</div>
									</td>
								</tr>
							</table>
							</form>
						</div>
					</td>
				</tr>
		</table>
	</div>
	<div class="table-wrapper">
		<button id="btn1" type="button" class="btn btn-default btn-sm" >&nbsp;全选
		</button>
		<button id="btn5" type="button" class="btn btn-default btn-sm" >&nbsp;提交
		</button>
		<table id="um-table-reload" class="table table-condensed">
			<thead>
				<tr>
					<th><p class="text-primary">#</p></th>
					<th><p class="text-primary">任务Id</p></th>
					<th><p class="text-primary">流程Id</p></th>
					<th><p class="text-primary">任务名称</p></th>
					<th><p class="text-primary">创建时间</p></th>
					<th><p class="text-primary">模板Id</p></th>
					<th><p class="text-primary">节点Id</p></th>
					<th><p class="text-primary">客户名称</p></th>
					<th><p class="text-primary">操作</p></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${workList.actWorkListVOs}" var="actcwl"
					varStatus="status">
					<tr>
						<td><input type="checkbox" name="checkbox" value="${actcwl.id}@${actcwl.processDefinitionId}${actcwl.taskDefinitionKey}"></td>
						<td>${actcwl.id}</td>
						<td>${actcwl.executionId}</td>
						<td>${actcwl.name}</td>
						<td><fmt:formatDate value="${actcwl.createTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${actcwl.processDefinitionId}</td>
						<td>${actcwl.taskDefinitionKey}</a></td>
						<td>${actcwl.processVariables.customerName}</td>
						<td>
							<div class="btn-group">
								<a name="handle,${actcwl.id}" title="处理"><i class="glyphicon glyphicon-edit"></i></a>
								<a name="turn,${actcwl.id}" title="转派"><i class="glyphicon glyphicon-retweet"></i></a>
								<a name="replace,${actcwl.id}" title="拒签"><i class="glyphicon glyphicon-remove"></i></a>
								<a name="pic,${actcwl.executionId},${actcwl.processDefinitionId},${actcwl.id}" title="流程图"><i class="glyphicon glyphicon-eye-open"></i></a>
							</div>		
						</td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
		<div  class="um-table-page-down">
				<nav>
				  <ul class="pager">
				    <li>
								<a href="worklist/queryworkList?pageNo=1&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">First</a>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.pageNo - 1 > 0}">
										<a href="worklist/queryworkList?pageNo=${page.pageNo - 1}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Previous</a>
									</c:when>
									<c:when test="${page.pageNo - 1 <= 0}">
										<a href="worklist/queryworkList?pageNo=1&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Previous</a>
									</c:when>
								</c:choose>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.totalPage==0}">
										<a href="worklist/queryworkList?pageNo=${page.pageNo}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
									<c:when test="${page.pageNo + 1 < page.totalPage}">
										<a href="worklist/queryworkList?pageNo=${page.pageNo + 1}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
									<c:when test="${page.pageNo + 1 >= page.totalPage}">
										<a href="worklist/queryworkList?pageNo=${page.totalPage}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
								</c:choose>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.totalPage==0}">
										<a href="worklist/queryworkList?pageNo=${page.pageNo}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Last</a>
									</c:when>
									<c:otherwise>
										<a href="worklist/queryworkList?pageNo=${page.totalPage}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Last</a>
									</c:otherwise>
								</c:choose>
				    </li>
				    <li>
				    	<span label label-info>${page.pageNo}/${page.totalPage}</span>
				    </li>
				  </ul>
				</nav>
			</div>	
		
	</div>
</body>
</html>
