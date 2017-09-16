<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	function doAdd(){
		//alert("增加...");
		$('#addStaffWindow').window("open");
	}
	//修改
	function doView(){
		
		$('#queryStaffWindow').window("open");
	}
	//作废
	function doDelete(){
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
			"${pageContext.request.contextPath}/staff/staffAction_deltag"
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
	
	function doRestore(){
		//获取多选行
		var arr=$('#grid').datagrid("getSelections");
		var ids=new Array();
		if (arr==null|| arr.length==0) {
			$.messager.alert("警告", "请至少选择一行", "warning")
		}else{
			for (var i = 0; i < arr.length; i++) {
				ids.push(arr[i].id);
			}
			//字符串
			var idStr=ids.join(",");
			//ajax 进行批量作废
			$.post(
			"${pageContext.request.contextPath}/staff/staffAction_backtag"
			,{'ids':idStr},function(data){
					if (data) {
						$("#grid").datagrid("clearChecked");//清空勾选框
						$('#grid').datagrid("reload");//重新加载当前页面
					}else{
						$.messager.alert("警告", "还原失败,系统维护中", "warning");
					}
				}		
			);
		}
	}
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center'
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center'
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="1"){
				return "有";
			}else{
				return "无";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="1"){
				return "正常使用"
			}else{
				return "已作废";
			}
		}
	}, {
		field : 'standard',
		title : '取派标准',
		width : 120,
		align : 'center'
	}, {
		field : 'station',
		title : '所谓单位',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [1,2,5],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath }/staff/staffAction_pageDate",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		//自定义电话号码校验器
		$.extend($.fn.validatebox.defaults.rules, { 
			telephone:{ 
				validator: function(value,param){ 
					 var regu =/^1[3|5|7|8]\d{9}$/;
					return regu.test(value); 
				}, 
				message: '手机号必须1开头11位数字' 
				},
			uniquePhone:{
				validator:function(value){
					var flag;
					$.ajax({
						url:"${pageContest.request.contextPath}/staff/staffAction_ajaxTelephone",
						type:'post',
						timeout : 60000,  
						data:{"telephone":value},
						async:false,
						success:function(data,textStatus, jqXHR){
							if (data) {
								flag=true;
							}else{
								flag=false;	
							}
						}
					});
					 if(flag){  
			            	// 样式效果
		                    $("#atel").removeClass('validatebox-invalid');  
		                } 
					return flag;
				},
				message:"手机号已存在,请重新输入"
			}
				}); 

		//提交添加或修改表单
		$("#save").click(function() {
					var falg =$("#addStaffForm").form("validate");
					if (falg) {
						//表单提交时设置默认生效
						$("#addStaffForm").submit();
						$('#addStaffWindow').window("close");
					}
				});
		//提交条件查询表单
		$("#query").click(function() {
			//json
			var jsondata={'name':$("#qname").val(),'telephone':$("#qtelephone").val(),
					'station':$("#qstation").val(),
					'standard':$("#qstandard").combobox('getValue')};
				$('#grid').datagrid('load',jsondata); 
				$('#queryStaffWindow').window("close");
			});
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '添加取派员',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        //窗口打开之前进行添加
        	onBeforeOpen:function(){
   			$("input[name='standard']").combobox({ 
   				//收取标准
   			url:"${pageContext.request.contextPath}/staff/staffAction_findAllStaffName", 
   			valueField:'name', 
   			textField:'name' 
       			});
	        },
	        //窗口关闭之前
	        onBeforeClose:function(){
	        	//窗口关闭前进行form清除
	        	
	        	$('#atel').validatebox({required: true, validType: ['telephone','uniquePhone' ]}); 
	        	$('#addStaffForm').form('clear');
	        	$("input[name='sid']").val("");
	        	
	        }
	    });
		// 查询取派员窗口
		$('#queryStaffWindow').window({
	        title: '添加取派员',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 270,
	        resizable:false,
	        //窗口打开之前进行添加
        	onBeforeOpen:function(){
   			$("input[name='standard']").combobox({ 
   			url:"${pageContext.request.contextPath}/staff/staffAction_findAllStaffName", 
   			valueField:'name', 
   			textField:'name' 
       			});
	        }
	    });
		
	});
	function doDblClickRow(rowIndex,rowData){
		$('#atel').validatebox({required: true, validType: 'telephone'}); 
		//打开显示修改div
		$('#addStaffWindow').window("open");
		//表单回显
		$('#addStaffWindow').form('load',rowData); 
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存/修改</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addStaffForm" method="post" action="${pageContext.request.contextPath }/staff/staffAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<tr>
						<td>姓名</td>
						<td>
						<input type="hidden" id="sid" name="id"/>
						<input type="text" name="name" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="atel" class="easyui-validatebox"
						data-options="validType:['telephone','uniquePhone'],required:'true'" /></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"
								 
						/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard"  data-options="editable:false"  />  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	
	<div class="easyui-window" title="查询收派员信息" id="queryStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-search" href="#" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="queryStaffForm" method="post" action="#">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<tr>
						<td>姓名</td>
						<td><input type="text" name="name" id="qname" /></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="qtelephone" /></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" id="qstation"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard" id="qstandard" data-options="editable:false"  />  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>	