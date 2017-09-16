<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>取派标准</title>
		<!-- 导入jquery核心类库 -->
		<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派标准信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [5,10,20],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/bc/standardAction_pageDate",
					idField : 'id',
					columns : columns,
					//双击行事件
					onDblClickRow:function(rowIndex, rowData){
						//打开显示修改div
						$('#addStaffWindow').window("open");
						//表单回显
						$('#addStaffWindow').form('load',rowData); 
					}
				});
				// 添加收货信息窗口
				$('#addStaffWindow').window({
			        title: '取派标准操作',
			        width: 600,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false,
			        onBeforeClose:function(){
			        	//窗口关闭前进行form清除
			        	$('#addStaffForm').form('clear');
			        	$('#grid').datagrid("clearChecked");//清除所有选择行
			        	$("#deltag").val("1");
			        }
			    });
				
				$("#save").click(function() {
					var falg =$("#addStaffForm").form("validate");
					if (falg) {
						//表单提交时设置默认生效
						$("#addStaffForm").submit();
						$('#addStaffWindow').window("close");
					}
				});
				
				
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					$('#addStaffWindow').window("open");
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					var arr=$('#grid').datagrid("getChecked");
					if (arr==null|| arr.length==0) {
						$.messager.alert("警告", "请选择要修改的一行", "warning")
					}else if (arr.length>1){
						$.messager.alert("警告", "只能选择一行", "warning")
					}else{
					$('#addStaffWindow').window("open");
					$('#addStaffWindow').form('load',arr[0]); 
					}
				}
			},{
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : function(){
					//获取多选行
					var arr=$('#grid').datagrid("getSelections");
					var ids=new Array();
					if (arr==null|| arr.length==0) {
						$.messager.alert("警告", "至少选择一行", "warning")
					}else{
						for (var i = 0; i < arr.length; i++) {
							ids.push(arr[i].id);
						}
						//字符串
						var idStr=ids.join(",");
						//ajax 进行批量作废
						$.post(
						"${pageContext.request.contextPath}/bc/standardAction_deltag"
						,{'ids':idStr},function(data){
								if (data) {
									$("#grid").datagrid("clearChecked");//
									$('#grid').datagrid("reload");
								}else{
									$.messager.alert("警告", "作废失败,系统维护中", "warning");
								}
							}		
						);
					}
				}
			},{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : function(){
					//获取多选行
					var arr=$('#grid').datagrid("getSelections");
					var ids=new Array();
					if (arr==null|| arr.length==0) {
						$.messager.alert("警告", "请选择要修改的一行", "warning")
					}else{
						for (var i = 0; i < arr.length; i++) {
							ids.push(arr[i].id);
						}
						//字符串
						var idStr=ids.join(",");
						//ajax 进行批量作废
						$.post(
						"${pageContext.request.contextPath}/bc/standardAction_backtag"
						,{'ids':idStr},function(data){
								if (data) {
									$("#grid").datagrid("clearChecked");//
									$('#grid').datagrid("reload");
								}else{
									$.messager.alert("警告", "还原失败,系统维护中", "warning");
								}
							}		
						);
					}
				}
			}];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true
			},{
				field : 'name',
				title : '标准名称',
				width : 120,
				align : 'center'
			}, {
				field : 'minweight',
				title : '最小重量',
				width : 120,
				align : 'center'
			}, {
				field : 'maxweight',
				title : '最大重量',
				width : 120,
				align : 'center'
			}, {
				field : 'minlength',
				title : '最小长度',
				width : 120,
				align : 'center'
			}, {
				field : 'maxlength',
				title : '最大长度',
				width : 120,
				align : 'center'
			}, {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operationtime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '操作单位',
				width : 120,
				align : 'center'
			} , {
				field : 'deltag',
				title : '是否生效',
				width : 120,
				align : 'center',
				formatter: function(value,row,index){
			         if (value==1){
			            return "已生效";
			         } else {
			            return "已作废";
			         }  			
		     	} 
			}] ];
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		<!-- 添加取派员窗体  -->
	<div class="easyui-window" title="对收派标准进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addStaffForm" method="post" action="${pageContext.request.contextPath }/bc/standardAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派标准信息</td>
					</tr>
					<tr>
						<td>标准名称</td>
						<td>
						<input type="hidden" name="id" id="id"  />
						<input type="hidden" name="deltag" id="deltag" />
						<input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
					</tr>
					<tr>
						<td>最小重量</td>
						<td><input id="minweight" type="text" name="minweight" class="easyui-numberbox"  
						data-options="min:1,precision:0,suffix:'kg',required:true" />
						   </td>
					</tr>
					<tr>
						<td>最大重量</td>
						<td><input id="maxweight" type="text" name="maxweight" class="easyui-numberbox" 
						data-options="max:500,precision:0,suffix:'kg',required:true" /></td>
					</tr>
					<tr>
						<td>最小长度</td>
						<td><input id="minlength" type="text" name="minlength" class="easyui-numberbox" 
						data-options="min:10,precision:0,suffix:'cm',required:true"/></td>
					</tr>
					<tr>
						<td>最大长度</td>
						<td><input id="maxlength" type="text" name="maxlength" class="easyui-numberbox" 
						data-options="max:1000,precision:0,suffix:'cm',required:true" /></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	</body>

</html>