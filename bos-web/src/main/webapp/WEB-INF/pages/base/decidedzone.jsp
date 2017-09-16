<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理定区/调度排班</title>
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
<script
	src="${pageContext.request.contextPath }/js/bosjs/bos-utils.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function doAdd(){
		$('#addDecidedzoneWindow').window("open");
	}
	
	function doEdit(){
		var arr=$('#grid').datagrid("getSelections");
		if (arr==null||arr.length==0) {
			$.messager.alert("警告!", "请钩选一行数据", "warning");
			return;
		}else if(arr.length>1){
			$.messager.alert("警告!", "只能选一行数据", "warning");
			return;
		}else{
			
			//打开编辑窗口
			$('#addDecidedzoneWindow').window("open");
			$("#subareaGrid").datagrid({
				url:'${pageContext.request.contextPath}/subarea/subareaAction_findAllSubarea'
			})
			//窗口打开之后
			$("#_decid").attr("readonly", true);//只读
			$('#_decid').validatebox({ validType: ''});
			//回显数据
			$("#addDecidedzoneForm").form("load",arr[0]);
			//下拉框回显
			$("#staffid").combobox('setValue', arr[0].staff.id); 
		}
		
		
	}
	function doDelete(){
		//获取多选行
		/*deleteRowDecidedzone("#grid","getSelections"
		 	,"${pageContext.request.contextPath}/decidedzone/decidedzoneAction_delDecidedzone") */
	 var arr=$("#grid").datagrid("getSelections");
		var ids=new Array();
		if (arr==null|| arr.length==0) {
			$.messager.alert("警告", "请至少选择一行", "warning")
		}else{
			for (var i = 0; i < arr.length; i++) {
				ids.push(arr[i].id);
			}
			//字符串
			var idStr=ids.join(",");
			//多表删除
			$.post("${pageContext.request.contextPath}/decidedzone/decidedzoneAction_delDecidedzone",
					{'ids':idStr},function(data){
					if (data) {
						$("#grid").datagrid("clearChecked");//清空勾选框
						$("#grid").datagrid("reload");//重新加载当前页面
					}else{
						$.messager.alert("警告", "还原失败,系统维护中", "warning");
					}
				}		
			);
		} 
	}
	
	function doSearch(){
		$('#searchWindow').window("open");
	}
	//左右关联
	function doAssociations(){
		var arr=$('#grid').datagrid("getSelections");
		if (arr==null||arr.length==0) {
			$.messager.alert("警告!", "请钩选一行数据", "warning");
			return;
		}else if(arr.length>1){
			$.messager.alert("警告!", "只能选一行数据", "warning");
			return;
		}else{
			//打开编辑窗口
			$('#customerWindow').window('open');
			$("#noassociationSelect").empty();
			$("#associationSelect").empty();
				//未关联客户信息
			$.post('${pageContext.request.contextPath}/decidedzone/decidedzoneAction_findNoAssociation',
					function(data){
						$("#noassociationSelect").append("<option value=''>--未关联客户--</option>");
					$(data).each(function(){
						$("#noassociationSelect").append("<option value="+this.id+">"+this.name+"</option>");
					})
			});
			//已关联客户信息
			$.post("${pageContext.request.contextPath}/decidedzone/decidedzoneAction_findUserAssociation",
					{"id":arr[0].id},function(data){
						$(data).each(function(){
							$("#associationSelect").
							append("<option value="+this.id+">"+this.name+"</option>");
					})
			})
			//窗口打开之后
			/* $("#_decid").attr("readonly", true);//只读
			$('#_decid').validatebox({ validType: ''});
			//回显数据
			$("#addDecidedzoneForm").form("load",arr[0]);
			//下拉框回显
			$("#staffid").combobox('setValue', arr[0].staff.id);  */
		}
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doEdit
	}/* ,{
		id : 'button-save',	
		text : '保存',
		iconCls : 'icon-save',
		handler : doSave
	} */,{
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-association',
		text : '关联客户',
		iconCls : 'icon-sum',
		handler : doAssociations
	}];
	// 定义列
	var columns = [ [{
		checkbox : true,
	},{
		field : 'id',
		title : '定区编号',
		width : 120,
		align : 'center'
	},{
		field : 'name',
		title : '定区名称',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'staff.name',
		title : '负责人',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.name;
		}
	}, {
		field : 'staff.telephone',
		title : '联系电话',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.telephone;
		}
	}, {
		field : 'staff.station',
		title : '所属公司',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.station;
		}
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [2,5,10],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/decidedzone/decidedzoneAction_pageDate",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,//双击行
			//onClickRow  : onClickRow,		//单级航
			//onAfterEdit  :  onAfterEdit
		});
		
		// 添加、修改定区
		$('#addDecidedzoneWindow').window({
	        title: '添加修改定区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 查询定区
		$('#searchWindow').window({
	        title: '查询定区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		$("#btn").click(function(){
			var jsondata= $("#searchform").serializeJson();
			$("#grid").datagrid("load",jsondata);
			$("#searchWindow").window("close");
		});
		
		//保存
		$("#save").click(function(){
			 var falg=$("#addDecidedzoneForm").form("validate");
			if (falg) {
				$("#addDecidedzoneForm").submit();
				$("#_decid").attr("readonly", false);//只读
				$("#addDecidedzoneForm").form("clear");//清空表单
				$('#grid').datagrid("clearChecked");//清除所有选择行
				$("#addDecidedzoneWindow").window("close");//关闭窗口
				$('#grid').datagrid("reload");
			} 
		});
		//左右移动
		$("#toRight").click(function () {
			$("#associationSelect").append($("#noassociationSelect option:selected"));
		})
		$("#toLeft").click(function () {
			$("#noassociationSelect").append($("#associationSelect option:selected"));
		})
		//提交客户关联数据
		$("#associationBtn").click(function(){
			var arr=$('#grid').datagrid("getSelections");
			$("#customerDecidedZoneId").val(arr[0].id);
			
			$("#associationSelect option").attr("selected","selected");
			$("#customerForm").submit();
			$('#grid').datagrid("reload");
			$('#customerWindow').window("close");
		});
		//自定义邮编校验器
		$.extend($.fn.validatebox.defaults.rules, { 
			//区域编码唯一性校验
			uniqueDecidedID:{
				validator:function(value){
					var flag;
					$.ajax({
						url:"${pageContext.request.contextPath}/decidedzone/decidedzoneAction_ajaxDecidedID",
						type:'post',
						timeout : 60000,  
						data:{"id":value},
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
		                    $("#pid").removeClass('validatebox-invalid');  
		                } 
					return flag;
				},
				message:"定区编号已存在,请重新输入"
				},
			
			
		})
	});

	
	function doDblClickRow(rowIndex, rowData){
		$('#association_subarea').datagrid( {
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			url : "${pageContext.request.contextPath}/subarea/subareaAction_pageDate?decidedzone.id="+rowData.id,
			columns : [ [{
				field : 'id',
				title : '分拣编号',
				width : 120,
				align : 'center'
			},{
				field : 'province',
				title : '省',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.province;
				}
			}, {
				field : 'city',
				title : '市',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.city;
				}
			}, {
				field : 'district',
				title : '区',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.district;
				}
			}, {
				field : 'addresskey',
				title : '关键字',
				width : 120,
				align : 'center'
			}, {
				field : 'startnum',
				title : '起始号',
				width : 100,
				align : 'center'
			}, {
				field : 'endnum',
				title : '终止号',
				width : 100,
				align : 'center'
			} , {
				field : 'single',
				title : '单双号',
				width : 100,
				align : 'center'
			} , {
				field : 'position',
				title : '位置',
				width : 200,
				align : 'center'
			} ] ]
		});
		$('#association_customer').datagrid( {
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			url : "${pageContext.request.contextPath}/decidedzone/decidedzoneAction_findUserAssociation?id="+rowData.id,
			columns : [[{
				field : 'id',
				title : '客户编号',
				width : 120,
				align : 'center'
			},{
				field : 'name',
				title : '客户名称',
				width : 120,
				align : 'center'
			}, {
				field : 'station',
				title : '所属单位',
				width : 120,
				align : 'center'
			}]]
		});
		
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div region="south" border="false" style="height:150px">
		<div id="tabs" fit="true" class="easyui-tabs">
			<div title="关联分区" id="subArea"
				style="width:100%;height:100%;overflow:hidden">
				<table id="association_subarea"></table>
			</div>	
			<div title="关联客户" id="customers"
				style="width:100%;height:100%;overflow:hidden">
				<table id="association_customer"></table>
			</div>	
		</div>
	</div>
	
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="定区添加修改" id="addDecidedzoneWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="addDecidedzoneForm" method="post" action="${pageContext.request.contextPath}/decidedzone/decidedzoneAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">定区信息</td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" id="_decid"  name="id" class="easyui-validatebox" 
						data-options="required:true,validType:'uniqueDecidedID'"/></td>
					</tr>
					<tr>
						<td>定区名称</td>
						<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
					</tr>
					<tr>
						<td>选择负责人</td>
						<td>
							<input class="easyui-combobox" name="staff.id"  id="staffid"
    							data-options="valueField:'id',textField:'name',
    							url:'${pageContext.request.contextPath}/staff/staffAction_findStaffName'" />  
						</td>
					</tr>
					<tr height="300">
						<td valign="top">关联分区</td>
						<td>
							<table id="subareaGrid"  class="easyui-datagrid" border="false" style="width:300px;height:300px" 
							data-options="url:'${pageContext.request.contextPath}/subarea/subareaAction_findAllSubarea',
							fitColumns:true,singleSelect:false">
								<thead>  
							        <tr>  
							            <th data-options="field:'sid',width:30,checkbox:true">编号</th>  
							            <th data-options="field:'addresskey',width:150">关键字</th>  
							            <th data-options="field:'position',width:200,align:'right'">位置</th>  
							        </tr>  
							    </thead> 
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 查询定区 -->
	<div class="easyui-window" title="查询定区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="searchform" method="post" action="#">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" data-options="required:true"/></td>
					</tr>
					<tr>
						<td>所属单位</td>
						<td>
							<input type="text"  class="easyui-combobox" id="regionid" name="staff.id"  
    							data-options="valueField:'id',textField:'station',mode:'remote',
    							url:'${pageContext.request.contextPath }/staff/staffAction_ajaxStaffList'" />  
						</td>
					</tr>
					<tr>
						<td>是否关联分区</td>
						<td>
						<select class="easyui-combobox" name="isAddSubarea" style="width:150px;">  
							    <option value="none">--请选择--</option>  
							    <option value="1">是</option>  
							    <option value="0">否</option>  
							</select> </td>
					</tr>
					<tr>
						<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<!-- 关联客户窗口 -->
	<div class="easyui-window" title="关联客户窗口" id="customerWindow" collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 400px;height: 300px;">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="customerForm" 
			action="${pageContext.request.contextPath }/decidedzone/decidedzoneAction_assigncustomerstodecidedzone" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="3">关联客户</td>
					</tr>
					<tr>
						<td>
							<input type="hidden" name="id" id="customerDecidedZoneId" />
							<select id="noassociationSelect" multiple="multiple" size="10">
								
							</select>
						</td>
						<td>
							<input type="button" value="》》" id="toRight"><br/>
							<input type="button" value="《《" id="toLeft">
						</td>
						<td>
							<select id="associationSelect" name="customerIds" multiple="multiple" size="10">
								<option value="none">--未关联客户--</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="3"><a id="associationBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联客户</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>