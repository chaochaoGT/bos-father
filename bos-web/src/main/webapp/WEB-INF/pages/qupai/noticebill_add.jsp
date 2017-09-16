<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加业务受理单</title>
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
	$(function(){
		$("body").css({visibility:"visible"});
		//三级联动
		loadCity(0,province)
		// 对save按钮条件 点击事件
		$('#save').click(function(){
			// 对form 进行校验
			if($('#noticebillForm').form('validate')){
				var province=document.getElementById("province").selectedOptions[0].text;
				var city=document.getElementById("city").selectedOptions[0].text;
				var district=document.getElementById("district").selectedOptions[0].text;
				$("#nprovince").val(province);
				$("#ncity").val(city);
				$("#ndistrict").val(district);
				$('#noticebillForm').submit();
			}
		});
		
	//自定义电话号码校验器
	$.extend($.fn.validatebox.defaults.rules, {
			telephone : {
				validator : function(value, param) {
					var regu = /^1[3|5|7|8]\d{9}$/;
					return regu.test(value);
				},
				message : '手机号必须1开头11位数字'
			},
		});
	
	//客户手机号查询
	$("#ntelephone").blur(function(){
		if (this.value=="") {
			$.messager.alert("警告", "请输入手机号", "warning");
			return;
		}
		$.post("${pageContext.request.contextPath}/noticeBill/noticeBillAction_ajaxCustomer",
			{"telephone":this.value},function(data){
			if (data==null) {
				$("#tel_sp").html("<font color='green'>新用户</font>");
				$("#customerId").val("")
				$("#customert").hide();
				$("#customerId").hide();
			}else{
				$("#customert").show();
				$("#customerId").show();
				$("#customerId").val(data.id).attr("readonly",true);
				$("#customerName").val(data.name).attr("readonly",true);
			}
		});
		
	})
	});
	function loadCity(value, target) {
		//清空下拉选项
		target.length = 1;
		district.legth = 1;
		if (value == "none")
			return;
		$.post("${pageContext.request.contextPath}/loadCity/loadCityAction_load",
			{"pid" : value}, function(data) {
				$(data).each(function() {
					var opt = document.createElement("option");
					opt.value = this.id;
					opt.innerHTML = this.name;
					$(target).append(opt);
				})
			});
	}
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false"
		border="false">
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton"
				plain="true">新单</a>
			<a id="edit" icon="icon-edit" href="${pageContext.request.contextPath }/page_qupai_noticebill.action" class="easyui-linkbutton"
				plain="true">工单操作</a>	
		</div>
	</div>
	<div region="center" style="overflow:auto;padding:5px;" border="false">
		<form id="noticebillForm" action="${pageContext.request.contextPath}/noticeBill/noticeBillAction_save" method="post">
			
			 <input type="hidden" name="nprovince"  id="nprovince" />
			 <input type="hidden" name="ncity"  id="ncity" />
			 <input type="hidden" name="ndistrict"  id="ndistrict" />
			<table class="table-edit" width="95%" align="center">
				<tr class="title">
					<td colspan="4">客户信息</td>
				</tr>
				<tr>
					<td>来电号码:</td>
					<td><input type="text" class="easyui-validatebox" name="telephone" id="ntelephone"
						data-options="validType:'telephone',required:'true'" />
						<span id="tel_sp"></span>
						</td>
					<td id='customert'>客户编号:</td>
					<td><input type="text" class="easyui-validatebox"  name="customerId" id="customerId"
						 /></td>
				</tr>
				<tr>
					<td>客户姓名:</td>
					<td><input type="text" class="easyui-validatebox" name="customerName" id="customerName"
						required="true" /></td>
				</tr>
				<tr class="title">
					<td colspan="4">货物信息</td>
				</tr>
				<tr>
					<td>品名:</td>
					<td><input type="text" class="easyui-validatebox" name="product"
						required="true" /></td>
					<td>件数:</td>
					<td><input type="text" class="easyui-numberbox" name="num"
						required="true" /></td>
				</tr>
				<tr>
					<td>重量:</td>
					<td><input type="text" class="easyui-numberbox" name="weight"
						required="true" /></td>
					<td>体积:</td>
					<td><input type="text" class="easyui-validatebox" name="volume"
						required="true" /></td>
				</tr>
				<tr>
					<td>取件地址</td>
					<!-- 表单提交将省市区中文信息发送给后台 -->
					<td colspan="3">
					 省&nbsp;
					 <select name="province" id="province" onchange="loadCity(value,city);">
					  <option value="none">--请选择--</option>
					 </select>&nbsp;
					 市&nbsp;
					 <select name="city" id="city" onchange="loadCity(value,district);">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;
					 区&nbsp;
					 <select name="district" id="district">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;详细地址
					<input type="text" class="easyui-validatebox" name="pickaddress" required="true" size="75"/>
				  </td>
				</tr>
				<tr>
					<td>到达城市:</td>
					<td><input type="text" class="easyui-validatebox" name="arrivecity"
						 /></td>
					<td>预约取件时间:</td>
					<td><input type="text" class="easyui-datebox" name="pickdate"
						data-options="editable:false" /></td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan="3"><textarea rows="5" cols="80" type="text" class="easyui-validatebox" name="remark"
						required="true" ></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>