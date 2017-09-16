

//查询窗口json序列化
$.fn.serializeJson = function() {
	var serializeObj = {};
	var array = this.serializeArray();
	var str = this.serialize();
	$(array).each(
			function() {
				if (serializeObj[this.name]) {
					if ($.isArray(serializeObj[this.name])) {
						serializeObj[this.name].push(this.value);
					} else {
						serializeObj[this.name] = [ serializeObj[this.name],
								this.value ];
					}
				} else {
					serializeObj[this.name] = this.value;
				}
			});
	return serializeObj;
};
//删除
	deleteRowDecidedzone=function (grid,evenType,url){
		//获取多选行
		var arr=grid.datagrid(evenType);
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
			$.post(url,{'ids':idStr},function(data){
					if (data) {
						$(grid).datagrid("clearChecked");//清空勾选框
						$(grid).datagrid("reload");//重新加载当前页面
					}else{
						$.messager.alert("警告", "还原失败,系统维护中", "warning");
					}
				}		
			);
		}
	};
	

	deleteRowDecidedzone = function(grid, evenType, url) {
	// 获取多选行
	var arr = grid.datagrid(evenType);
	var ids = new Array();
	if (arr == null || arr.length == 0) {
		$.messager.alert("警告", "请至少选择一行", "warning")
	} else {
		for (var i = 0; i < arr.length; i++) {
			ids.push(arr[i].id);
		}
		// 字符串
		var idStr = ids.join(",");
		// ajax 进行批量作废
		$.post(url, {
			'ids' : idStr
		}, function(data) {
			if (data) {
				$(grid).datagrid("clearChecked");// 清空勾选框
				$(grid).datagrid("reload");// 重新加载当前页面
			} else {
				$.messager.alert("警告", "还原失败,系统维护中", "warning");
			}
		});
	}
}


//三级联动
	$.fn.loadCity=function (url,value,target,district){
	//清空下拉选项
	target.length=1;
	district.legth=1;
	if (value=="none")return;
	$.post(url,
		{"pid":value},function(data){
		$(data).each(function(){
			var opt=new option();
			opt.value=data.id;
			opt.innerHTML=data.name;
			$(target).apppend(opt);
		})
		
	});
}

