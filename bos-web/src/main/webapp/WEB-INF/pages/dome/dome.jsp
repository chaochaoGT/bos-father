<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>页面练习</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css" >
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"></script>

</head>

	<SCRIPT type="text/javascript">
	var setting = {
			
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag){
					//  treeNode 每一个菜单json 对象
					   if(treeNode.page!=undefined&&treeNode.page!=""){
				        	  var flag =  $("#tt").tabs("exists",treeNode.name);
					            if(!flag){
					            	//  创建
					             $("#tt").tabs("add",{
								  title:treeNode.name,
								  //  content 内容 添加嵌套页面<iframe>
								  content:'<div style="width:100%;height:100%;overflow:hidden;">'
										+ '<iframe src="'
										+ treeNode.page
										+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>',
								  closable:true
							  });
					            }else{
					            	// 存在选中当前的tabs
					            	 $("#tt").tabs("select",treeNode.name);
					            }
				         }
				}
			},
			data: {
				simpleData:{
					enable:true
				}
			}
		};
	$(function(){
		 //  发送ajax  才可以访问  xx.json 文件
		 $.post("${pageContext.request.contextPath}/json/menu.json",function(data){
		      $.fn.zTree.init($("#treeDemo"), setting, data);
		 },"json");
	 });
	</SCRIPT>
	
<body class="easyui-layout"> 

	<div data-options="region:'north',title:'xxx',split:false" style="height:100px;"></div> 
	
	<div data-options="region:'south',title:'South Title',split:false" style="height:100px;"></div>
	 
	<div  data-options="region:'west',title:'菜单管理',split:false" style="width:200px;">
		
		<div id="aa" class="easyui-accordion" > 

			<div title="基本功能" data-options="iconCls:'icon-mini-add',selected:true" > 
				<ul id="treeDemo" class="ztree"></ul>
			</div> 
			
			<div title="Title1" data-options="iconCls:'icon-mini-add'" "> 
				Title1
			</div> 
			
			<div title="Title2" data-options="iconCls:'icon-mini-add'"> 
			
			content2 
			
			</div> 
			
		</div> 
	</div> 
	
	<div  id="tt" class="easyui-tabs" data-options="region:'center',title:'消息中心'" style="background:#eee;">
	</div> 

</body>

</html>