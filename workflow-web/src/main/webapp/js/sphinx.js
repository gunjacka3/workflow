/**
 * Sphinx Framework
 * 
 * @description Sphinx Framework
 * @author guorui@git.com.cn
 * @version 1.0
 */

/**
 * Events类：定义UI组件的公共事件
 * 
 * _initialize_:内部初始化方法
 * show:显示组件
 * hide:隐藏组件
 * addClass:添加css类
 * removeClass:移除css类
 * hasClass:判断是否有某个css类
 * toggleClass:切换css样式
 * toggle:切换响应
 * bind:绑定事件
 * unbind:取消事件绑定
 * getElementId:获取元素ID
 * getElementTag:获取元素TAG
 */
var Events = {
	_initialize_ : function(el, settings) {
		this.el = el || null;
		this.settings = settings || {};
	},
	show : function() {
		this.el.show();
		return this;
	},
	hide : function() {
		this.el.hide();
		return this;
	},
	addClass : function(className) {
		this.el.addClass(className);
		return this;
	},
	removeClass : function(className) {
		this.el.removeClass(className);
		return this;
	},
	hasClass : function(className) {
		this.el.hasClass(className);
		return this;
	},
	toggleClass : function(className) {
		this.el.toggleClass(className);
		return this;
	},
	toggle : function() {
		this.el.toggle();
		return this;
	},
	bind : function(eventType, handler) {
		this.el.bind(eventType, handler);
		return this;
	},
	unbind : function(eventType, handler) {
		this.el.unbind(eventType, handler);
		return this;
	},
	getElementId : function() {
		return this.el[0].id;
	},
	getElementTag : function() {
		return this.el[0].tagName;
	}
};

/**
 * Tree类
 * 
 * examples:
 * 1.静态初始化
 * var settings = {
 * 		zNodes:{name:"系统",children:[{name:"HR系统"},{name:"OA系统"}]},
 * 		callback:{
 * 			beforeClick:beforeClick,
 * 			beforeRightClick:beforeRightClick
 * 		}
 * };
 * var tree = new Tree($('#tree'), settings);
 * 
 * 2.动态初始化
 * var settings = {
 *		dynamic:true,
 *		url:"http://localhost:8080/webapp/ztree",
 *		success:onSuccess,
 *		callback:{
 *			beforeClick:beforeClick,
 *			beforeRightClick:zTreeBeforeRightClick
 *		}
 *	};
 * var tree = new Tree($('#tree'), settings);
 */
var Tree = function(el, settings){
	this.ztreeObj = null;
	this.options = {dynamic:false,url:null,zNodes:{},callback:{}};
	this._initialize_.apply(this, arguments);
	this.settings = $.extend(options, settings);
	this.initialize(this.settings);
};
$.extend(Tree.prototype, Events, {
	initialize: function(settings){
		var zNodes = null;
		if(!settings.dynamic){
			zNodes = settings.zNodes;
		}else{
			if(settings.url){
				$.ajax({
				    type: 'GET',
				    url: settings.url,
				    data: settings.data,
				    async: false,
				    success:function(response){
				    	if(response.code == 0){
				    		zNodes = settings.success(response.data);
				    	}
			        }
				});
			}
		}
		this.ztreeObj = $.fn.zTree.init(this.el, settings, zNodes);
	},
	getTreeObj: function(){
		return this.ztreeObj;
	},
	reload: function(){
		this._initialize(this.settings);
	},
	expandAll: function(expandFalg){
		this.ztreeObj.expandAll(expandFalg);
	},
	addNodes: function(parentNode, newNode, isSilent){
		this.ztreeObj.addNodes(parentNode, newNode, isSilent);
	},
	removeNode: function(treeNode){
		this.ztreeObj.removeNode(treeNode);
	},
	updateNode: function(treeNode){
		this.ztreeObj.updateNode(treeNode);
	},
	checkNode: function(treeNode, checked){
		this.ztreeObj.checkNode(treeNode, checked, false);
	},
	reloadExpand: function(){
		 //保存已经展开树
		 var opens ={};
		 var nodes = this.ztreeObj.transformToArray(this.ztreeObj.getNodes());
		 for(var key in nodes){ 
			 if(nodes[key].isHover){
				 opens[nodes[key].level+"_"+nodes[key].id] = true;
			 }else{
				 opens[nodes[key].level+"_"+nodes[key].id] = nodes[key].open;
			 }
		 }
		 //重新加载树
		 this.reload();
		 var nodes = this.ztreeObj.transformToArray(this.ztreeObj.getNodes());
		 var newNodeTids ={};
		 for(var key in nodes){ 
			 newNodeTids[nodes[key].level+"_"+nodes[key].id] = nodes[key].tId;
		 }
		 //展开新树
		 for(var key  in  opens){
			 if(opens[key]){
				 var newNode = this.ztreeObj.getNodeByTId(newNodeTids[key]);
				 if(newNode != null){
					 this.ztreeObj.expandNode(newNode,true,false,false);
				 }
			 }
		 }
	}
});


/**
 * DataTable类
 * @Deprecated 2015/4/9
 * 
 * examples:
 * var settings = {
 *    fields: ["groupId","groupName","groupDesc","groupCategory","custNum","dataSrcCd","lastOperType","lastOperDt","queryText"],
 *    method: "POST",
 *    url: "${ctx}/customer/group/list"
 * };
 * var dataTable = new DataTable($('#datatable'), settings);
 * 
 */
var DataTable = function(el, settings) {
	this.tableObj = null;
	this.options = {
		lengthChange : false, //是否允许改变分页大小
		searching : false, //是否允许搜索
		ordering : false, //是否允许排序
		order : [ [ 0, "asc" ] ],
		paging : true, //是否允许分页
		scrollX : true, //是否允许横向滚动
		scrollCollapse : true,
		processing : true,
		serverSide : true,
		fields : null,
		columns : null,
		params : null,
		errorMsg : "加载数据失败。"  //错误提示消息
	};
	this._initialize_.apply(this, arguments);
	this.settings = $.extend(this.options, settings);
	this.initialize(this.settings);
};
$.extend(DataTable.prototype, Events, {
	initialize: function(settings){
		if(settings.url) {
			console.debug(settings);
			if(settings.fields && settings.fields instanceof Array){
				var cols = [];
				for(var i=0; i<settings.fields.length; i++){
					cols.push({"data":settings.fields[i]});
				}
				settings['columns'] = cols;
			}
			this.settings['ajax'] = function(data, render, options) {
				var params = {
					draw : data.draw,
					start : data.start,
					length : data.length,
					order : data.order
				};
				$.extend(params, settings.params);
				$.ajax({
					type : settings.method,
					url : settings.url,
					dataType : "json",
					data : params,
					success : function(resp) {
						render(resp);
					},
					error : function(jqXHR, status, errorThrown) {
						alert(settings.errorMsg);
					}
				});
			};
			this.tableObj = this.el.dataTable(settings);
		}
	},
	getTableObj: function(){
		return this.tableObj;
	}
});

/**
 * DynamicTab类
 * 
 * examples:
 * 1.HTML
 * <div id="tabs">
 * 	 <ul>
 *	   <li><a href="#tabs-1">Tab1</a></li>
 * 	 </ul>
 * 	 <div id="tabs-1">
 * 	   <p>Hello world!</p>
 * 	 </div>
 * </div>
 * 
 * 2.JS
 * var tabs = new DynamicTab($('#tabs'));
 * 
 */
var DynamicTab = function(el, settings) {
	this.tabArray = new Array();
	this.tabObj = null;
	this.options = {
		wrapperClass: 'tab-wrapper',
		wrapperHeight: null,
		heightStyle: "fill"
	};
	this._initialize_.apply(this, arguments);
	this.settings = $.extend(this.options, settings);
	this.initialize(this.settings);
};
$.extend(DynamicTab.prototype, Events, {
	initialize: function(settings) {
		var tabArray = this.tabArray;
		this.tabObj = this.el.tabs(settings);
		var panels = this.tabObj.find('div.ui-tabs-panel');
		if(settings.wrapperClass) {
			panels.addClass(settings.wrapperClass);
		}
		panels.each(function() {
			tabArray.push($(this).attr('id'));
		});
		
		if(settings.wrapperHeight) {
			this.oriHeight = settings.wrapperHeight;
			panels.css({"height":settings.wrapperHeight,"overflow":"auto"});
			this.tabObj.on("tabsactivate", function(event, ui) {
				ui.newPanel.css({"height":settings.wrapperHeight,"overflow":"auto"});
			});
		}
		
		this.tabObj.delegate("span.ui-icon-close", "click", {'tabObj' : this.tabObj}, function(event) {
			var current = $(this).closest("li");
			var tabObj = event.data.tabObj;
			
			var tabId = current.remove().attr("aria-controls");
			$("#" + tabId).remove();
			
			var index = $.inArray(tabId, tabArray);
			tabArray.splice(index, 1);
			
			tabObj.tabs("refresh");
		});
	},
	addTab: function(tabId, title, url) {
		var index = $.inArray(tabId, this.tabArray);
		if(index > 0) {
			this.tabObj.tabs("option", "active", index);
		} else {
			var tabTemplate = "<li><a href='#{tabId}'><em class='fl'>#{title}</em><span style='float:right;margin-top:5px;cursor:pointer;' class='ui-icon ui-icon-close'></span></a></li>";
			var contentTemplate = "<div id='#{tabId}'><iframe scrolling='auto' frameborder='0' class='tab-frame' height='100%' width='100%' src='#{url}'></iframe></div>";
			var li = $(tabTemplate.replace(/#\{tabId\}/g, "#" + tabId).replace(/#\{title\}/g, title));
			var content = $(contentTemplate.replace(/#\{tabId\}/g, tabId).replace(/#\{url\}/g, url));
			this.tabObj.find(".ui-tabs-nav").append(li);
			this.tabObj.append(content);
			if(this.settings.wrapperClass) {
				this.tabObj.find('div.ui-tabs-panel').not('.'+this.settings.wrapperClass).addClass(this.settings.wrapperClass);
			}
			this.refresh();
			this.tabArray.push(tabId);
			
			this.tabObj.tabs("option", "active", this.tabArray.length -1);
		}
	},
	addStaticTab: function(tabId, title, html) {
		var index = $.inArray(tabId, this.tabArray);
		if(index > 0) {
			this.tabObj.tabs("option", "active", index);
		} else {
			var tabTemplate = "<li><a href='#{tabId}'><em class='fl'>#{title}</em><span style='float:right;margin-top:5px;cursor:pointer;' class='ui-icon ui-icon-close'></span></a></li>";
			var contentTemplate = "<div id='#{tabId}'>#{tabContent}</div>";
			var li = $(tabTemplate.replace(/#\{tabId\}/g, "#" + tabId).replace(/#\{title\}/g, title));
			var content = $(contentTemplate.replace(/#\{tabId\}/g, tabId).replace(/#\{tabContent\}/g, html));
			this.tabObj.find(".ui-tabs-nav").append(li);
			this.tabObj.append(content);
			if(this.settings.wrapperClass) {
				this.tabObj.find('div.ui-tabs-panel').not('.'+this.settings.wrapperClass).addClass(this.settings.wrapperClass);
			}
			this.refresh();
			this.tabArray.push(tabId);
			
			this.tabObj.tabs("option", "active", this.tabArray.length -1);
		}
	},
	refresh: function() {
		this.tabObj.tabs("refresh");
	},
	resize: function(newHeight) {
		if(newHeight > 0 && newHeight< this.oriHeight) {
			this.settings.wrapperHeight = newHeight;
		} else {
			this.settings.wrapperHeight = this.oriHeight;
		}
		this.refresh();
	}
});

