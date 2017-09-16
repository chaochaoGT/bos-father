<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
	src="${pageContext.request.contextPath }/js/poi/jquery.ocupload-min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function doAdd(){
		$('#addRegionWindow').window("open");
	}
	//修改
	function doView(){
		var arr=$('#grid').datagrid("getSelections");
		if (arr==null||arr.length==0) {
			$.messager.alert("警告!", "请钩选一行数据", "warning");
			return;
		}else if(arr.length>1){
			$.messager.alert("警告!", "只能选一行数据", "warning");
			return;
		}else{
			
			//窗口打开之前
			$('#pid').validatebox({required: true, validType: ['postID']});
			$('#postcode').validatebox({required: true, validType: ['postcode']});
			//打开编辑窗口
			$('#addRegionWindow').window("open");
			//回显数据
			$("#addRegionForm").form("load",arr[0]);
			//限制不可修改项
			//$('#pid').validatebox({editable:false});
			$("#pid").attr("readonly", true);
		}
	}
	//删除
	function doDelete(){
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
			"${pageContext.request.contextPath}/region/regionAction_Delete"
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
	//导出PDF
	function doExport() {
		alert("asdfa");
		location.href="${pageContext.request.contextPath}/region/regionAction_doExportPDF";
	}function doExport1() {
		alert("asdfa");
		location.href="${pageContext.request.contextPath}/region/regionAction_doExportPDF1";
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	},{
		id : 'button-export',
		text : '导出p',
		iconCls : 'icon-undo',
		handler : doExport
	},{
		id : 'button-export',
		text : '导出rep',
		iconCls : 'icon-undo',
		handler : doExport1
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center'
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center'
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center'
	}, {
		field : 'postcode',
		title : '邮编',
		width : 120,
		align : 'center'
	}, {
		field : 'shortcode',
		title : '简码',
		width : 120,
		align : 'center'
	}, {
		field : 'citycode',
		title : '城市编码',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/region/regionAction_pageDate",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加、修改区域窗口
		$('#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	      //窗口关闭之前
	        onBeforeClose:function(){
	        	//窗口关闭前进行form清除
	        	$("#pid").attr("readonly", false);
	        	$('#pid').validatebox({required: true,disable:false, validType: ['postID','uniquePostID']});
				$('#postcode').validatebox({required: true, validType: ['postcode','uniquePostcode']});
				$('#grid').datagrid("clearChecked");//清除所有选择行
	        	$('#addRegionForm').form('clear');
	        	$("input[name='pid']").val("");
	        	
	        }
	    });
		//提交添加或修改表单
		$("#save").click(function() {
					var falg =$("#addRegionForm").form("validate");
					if (falg) {
						//表单提交时设置默认生效
						$("#addRegionForm").submit();
						$('#addRegionWindow').window("close");
					}
				});
		//文件上传
		 $("#button-import").upload({  
             name: 'upload',  
             action: '${pageContext.request.contextPath}/region/regionAction_oneclickupload',//  后台接受文件地址
             enctype: 'multipart/form-data', //上传类型
             //长传之前校验
             onSelect: function (self, element) {  
					this.autoSubmit=false;
					var reg=/^(.+\.xls|.+\.xlsx)$/;
					if (reg.test(this.filename())) {
						this.submit();
					}else{
						$.messager.alert("警告!", "请上传表格文件", "warning" );
					}
             },  
             //长传成功后
             onComplete: function (data, self, element) {  
                if ("true"==data) {
                	$('#grid').datagrid("reload");
					$.messager.alert("恭喜您!", "文件上传成功","info");
				}else{
					$.messager.alert("请稍后!", "系统异常....","warning");
				}
             }  
         });  	
		
		//自定义邮编校验器
			$.extend($.fn.validatebox.defaults.rules, { 
				postcode:{ 
					validator: function(value,param){ 
						 var regu =/^\d{6}$/;
						return regu.test(value); 
					}, 
					message: '邮编号必须6位数字' 
					},
				uniquePostcode:{
					validator:function(value){
						var flag;
						$.ajax({
							url:"${pageContest.request.contextPath}/region/regionAction_ajaxPostcode",
							type:'post',
							timeout : 60000,  
							data:{"postcode":value},
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
			                    $("#postcode").removeClass('validatebox-invalid');  
			                } 
						return flag;
					},
					message:"邮编号已存在,请重新输入"
				},
			postID:{ 
				validator: function(value,param){ 
					 var regu =/^QY\d{3}$/;
					return regu.test(value); 
				}, 
				message: '区域编号必须QY开头+3位数字' 
				},
				//区域编码唯一性校验
				uniquePostID:{
					validator:function(value){
						var flag;
						$.ajax({
							url:"${pageContest.request.contextPath}/region/regionAction_ajaxPostID",
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
					message:"区域编号已存在,请重新输入"
					},
			});
	});

	function doDblClickRow(rowIndex, rowData){
		$('#pid').validatebox({required: true, validType: ['postID']});
		$('#postcode').validatebox({required: true, validType: ['postcode']});
		//打开编辑窗口
		$('#addRegionWindow').window("open");
		//回显数据
		$("#addRegionForm").form("load",rowData);
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addRegionForm" method="post" action="${pageContext.request.contextPath }/region/regionAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>区域编码</td>
						<td><input type="text" id="pid" name="id" class="easyui-validatebox" 
							data-options="validType:['postID','uniquePostID']"/></td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" class="easyui-validatebox " data-options="required:true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district" class="easyui-validatebox " data-options="required:true"/></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" id="postcode" name="postcode" class="easyui-validatebox" 
						data-options="validType:['postcode','uniquePostcode']"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" class="easyui-validatebox " data-options="required:true"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode" class="easyui-validatebox " data-options="required:true"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>