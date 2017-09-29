<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<form id="create-process-form" >
			<table  id="um-table-reload" class="table table-condensed">
				<tr>
					<td>
						<h4><span class="label label-primary">流程信息</span></h4>
					</td>
				</tr>
				<tr>
					<td>
					<div class="input-group">
					  <span class="input-group-addon" id="basic-addon1">流程模板</span>
					  <input name="pdfKey" type="text" class="form-control" placeholder="pdfKey" value="${pdfKey}" aria-describedby="basic-addon1" readonly="true">
					</div>
					</td>
				</tr>
			</table>
			<hr/>
			<table id="um-table-reload" class="table table-condensed">
					<tr>
						<td>
							<h4><span class="label label-primary">流程变量</span></h4>
						</td>
					</tr>
					<c:forEach items="${actFromInfos}" var="actform" varStatus="status">
						<tr>
							<td>
								<c:if test='${actform.filedType!="enum"}'>
									<div class="input-group input-group-sm">
									  <span class="input-group-addon" id="sizing-addon3">${actform.filedName}</span>
										  	<input type="text" class="form-control" name="${actform.filedId}.${actform.filedType}" value="${actform.filedValue}" placeholder="${actform.filedId}" aria-describedby="sizing-addon3" />
									  <span class="input-group-addon" id="sizing-addon3">${actform.filedType}</span>
									</div>
								</c:if>
								<c:if test='${actform.filedType=="enum"}'>
									<div class="input-group input-group-sm">
										<span class="input-group-addon" id="sizing-addonx">${actform.filedName}</span>
									      <select id="${actform.filedId}" name ="${actform.filedId}" class="form-control" aria-describedby="sizing-addonx">
										       <c:forEach items="${actform.values}" var="value" varStatus="status" >
										       		<c:if test='${status.index==0}'>
										       			<option value ="${value.valueId}" selected="selected">${value.valueName}</option>
										       		</c:if>
										       		<c:if test='${status.index!=0}'>
										       		  	<option value ="${value.valueId}">${value.valueName}</option>
										       		</c:if>
										       </c:forEach>
									      </select>
					   				</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
			</table>
			
		</form>
