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
	$(function(){
		$("body").css({visibility:"visible"});
		$('#save').click(function(){
			$('#form').submit();
		});
		
	//  角色数据checkbox生成
		 $.post("${pageContext.request.contextPath}/role/roleAction_ajaxList",function(data){
			 $(data).each(function(){
				 //  List<Role>
				 $("#grantRoles").append("<input name='roleIds' type='checkbox' value='"+this.id+"'>"+this.name+"</input>&nbsp;&nbsp;");
			 });
		 });

		//自定义账号/电话号码校验器
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
						url:"${pageContest.request.contextPath}/user/userAction_ajaxTelephone",
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
						$("#telephone").removeClass('validatebox-invalid');
					}
					return flag;
				},
				message:"手机号已存在,请重新输入"
			},
			uniqueEmail:{
				validator:function(value){
					var flag;
					$.ajax({
						url:"${pageContest.request.contextPath}/user/userAction_ajaxUserEmail",
						type:'post',
						timeout : 60000,
						data:{"email":value},
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
						$("#username").removeClass('validatebox-invalid');
					}
					return flag;
				},
				message:"账号已存在,请重新输入"
			}
		});

	});
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
		</div>
	</div>
    <div region="center" style="overflow:auto;padding:5px;" border="false">
       <form id="form" action="${pageContext.request.contextPath }/user/userAction_save" method="post" >
           <table class="table-edit"  width="95%" align="center">
           		<tr class="title"><td colspan="4">基本信息</td></tr>
	           	<tr><td>账号:</td><td>
	           	     <input type="text" name="email" id="username" class="easyui-validatebox"
							data-options="validType:'uniqueEmail',required:'true'" />
				</td>
					<td>口令:</td><td><input type="password" name="password" id="password" class="easyui-validatebox" required="true" validType="minLength[3]" /></td></tr>
				<tr class="title"><td colspan="4">其他信息</td></tr>
	           	<tr><td>工资:</td><td><input type="text" name="salary" id="salary" class="easyui-numberbox" /></td>
					<td>生日:</td><td><input type="text" name="birthday" id="birthday" class="easyui-datebox" /></td></tr>
	           	<tr><td>性别:</td><td>
	           		<select name="gender" id="gender" class="easyui-combobox" style="width: 150px;">
	           			<option value="">请选择</option>
	           			<option value="男">男</option>
	           			<option value="女">女</option>
	           		</select>
	           	</td>
					<td>单位:</td><td>
					<select name="station" id="station" class="easyui-combobox" style="width: 150px;">
	           			<option value="">请选择</option>
	           			<option value="总公司">总公司</option>
	           			<option value="分公司">分公司</option>
	           			<option value="厅点">厅点</option>
	           			<option value="基地运转中心">基地运转中心</option>
	           			<option value="营业所">营业所</option>
	           		</select>
				</td></tr>
				<tr>
					<td>联系电话</td>
					<td colspan="3">
						<input type="text" name="telephone" id="telephone" class="easyui-validatebox"
							   data-options="validType:['telephone','uniquePhone'],required:'true'" />
					</td>
				</tr>
				<tr>
					<td>用户角色</td>
					<td colspan="3" id="grantRoles">
						
					</td>
				</tr>
	           	<tr><td>备注:</td><td colspan="3"><textarea name="remark" style="width:80%"></textarea></td></tr>
           </table>
       </form>
	</div>
</body>
</html>