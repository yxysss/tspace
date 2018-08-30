	function goback() {
		window.location.href = "/tspace/User-mobile/index.jsp";
		// window.location.href = document.referrer;
	}
	function cdate(id) {
		redate = parseInt(id);
		chosen = 0;
		pick1 = 0;
		starttime1 = "";
		endtime1 = "";
		pick2 = 0;
		starttime2 = "";
		endtime2 = "";
		$("input[name=starttime]").val("");
		$("input[name=endtime]").val("");
		for (var i = 1; i <= 7; i ++) {
			var datex = document.getElementById("date"+i);
			datex.style.background="#f6f6f6";
		}
		var datex = document.getElementById("date"+id);
		datex.style.background="#00ffff";
		for (var i = (id-1)*30; i < id*30; i ++) {
			var k = i-(id-1)*30;
			var text = document.getElementById("time"+k);
			if (availableroom[i] == '1') {
				text.style.backgroundColor="#ff6a6a";
			} else {
				text.style.backgroundColor="#00ff40";
			}
		}
	}
	
	function ctime(id) {
		// 取得时间段的起止时间
		var pickid = id+1+redate*100;
		var date2 = new Date();
		var day0 = redate;
		date2.setDate(date2.getDate()+day0);
		var time0 = parseInt(id+1);
		var hour0 = parseInt(time0/2)+7;
		var displaystart = (date2.getMonth()+1).toString()+"月"+(date2.getDate()).toString()+"日 "+hour0.toString()+"时";
		if (time0%2 == 0) displaystart = displaystart + "00分";
		else displaystart = displaystart + "30分";
		var hour1 = parseInt((time0-1)/2)+8;
		var displayend = (date2.getMonth()+1).toString()+"月"+(date2.getDate()).toString()+"日 "+hour1.toString()+"时";
		if (time0%2 == 0) displayend = displayend + "30分";
		else displayend = displayend + "00分";
		var textclick = document.getElementById("time"+id);
		var color = textclick.style.backgroundColor;
		if (textclick.style.backgroundColor == "rgb(255, 106, 106)") {
			return ;
		}
		if (chosen == 0) { // 之前是0选中的情况
			chosen = 1;
			pick1 = pickid;
			starttime1 = displaystart;
			endtime1 = displayend;
			$("input[name=starttime]").val(displaystart);
			$("input[name=endtime]").val(displayend);
			textclick.style.backgroundColor = "yellow";
		} else {
			if (chosen == 1) { // 之前选中了一个时间段
				if (pick1 != 0) { // 选中了pick1
					if (pick1 != pickid) { // 新选中的不是之前选中的
						if (checkschedule(pick1, pickid) == false) { // 判断是否是一个空闲的连续时间段
							return ;
						}
						chosen = 2;
						pick2 = pickid;
						starttime2 = displaystart;
						endtime2 = displayend;
						var b, e;
						if (pick1>pick2) {
							$("input[name=starttime]").val(displaystart);
							b = pick2%100-1; e = pick1%100-1;
						} else {
							$("input[name=endtime]").val(displayend);
							b = pick1%100-1; e = pick2%100-1;
						}
						for (var tid = b; tid <= e; tid ++) {
							var textd = document.getElementById("time"+tid);
							textd.style.backgroundColor = "yellow";
						}
					} else { // 否则取消之前选中的
						chosen = 0;
						pick1 = 0;
						pick2 = 0;
						$("input[name=starttime]").val("");
						$("input[name=endtime]").val("");
						textclick.style.backgroundColor = "#00ff40";
					}
					
				} else {
					if (pick2 != pickid) { // pick2同理
						if (checkschedule(pick2, pickid) == false) { // 判断是否是一个空闲的连续时间段
							return ;
						}
						chosen = 2;
						pick1 = pickid;
						starttime1 = displaystart;
						endtime1 = displayend;
						var b, e;
						if (pick2>pick1) {
							$("input[name=starttime]").val(displaystart);
							b = pick1%100-1; e = pick2%100-1;
						} else {
							$("input[name=endtime]").val(displayend);
							b = pick2%100-1; e = pick1%100-1;
						}
						for (var tid = b; tid <= e; tid ++) {
							var textd = document.getElementById("time"+tid);
							textd.style.backgroundColor = "yellow";
						}
					} else {
						chosen = 0;
						pick1 = 0;
						pick2 = 0;
						$("input[name=starttime]").val("");
						$("input[name=endtime]").val("");
						textclick.style.backgroundColor = "#00ff40";
					}
				}
			} else { // 之前选中了两个时间段
				if (pick1 == pickid || pick2 == pickid) {
					var b, e, p;
					if (pick1 == pickid) p = pick2;
					else p = pick1;
					if (pick1>pick2) {
						b = pick2%100-1; e = pick1%100-1;
					} else {
						b = pick1%100-1; e = pick2%100-1;
					}
					for (var tid = b; tid <= e; tid ++) {
						var textd = document.getElementById("time"+tid);
						if (tid != p%100-1) textd.style.backgroundColor = "#00ff40";
					}
					if (pick1 == pickid) {
						pick1 = 0;
						$("input[name=starttime]").val(starttime2);
						$("input[name=endtime]").val(endtime2);
					}
					else {
						pick2 = 0;
						$("input[name=starttime]").val(starttime1);
						$("input[name=endtime]").val(endtime1);
					}
					chosen = 1;
				}
			}
		}
	}
	
	function checkschedule(a, b) {
		var sstart = (a<b?a:b)-1-redate*100;
		var eend = (a>b?a:b)-1-redate*100;
		var i = 0;
		for (i = sstart; i <= eend; i ++) {
			var aspan = document.getElementById("time"+i);
			if (aspan.style.backgroundColor == "rgb(255, 106, 106)") return false;
		}
		return true;
	}
	
	function commit() {
		// 检查提交申请时的日期是否与生成页面时的日期一致
		var date1 = new Date();
		var day1 = date1.getDate();
		if (day1 != day0) {
			// alert("日期已发生变化，需要刷新页面！");
			$("#popup00").popup('open');
			location.reload();
			return ;
		}
		if (chosen == 0) {
			// alert("请选择使用的时间段");
			$("#popup01").popup('open');
			return ;
		}
		var reason = document.getElementById("reason").value+"";
		var participants = document.getElementById("members").value+"";
		// alert("reason=" + reason.length);
		if (participants.length > 50) {
			$("#popup02").popup('open');
			return ;
		}
		if (reason.length > 50) {
			$("#popup03").popup('open');
			return ;
		}
		document.getElementById("croomname").innerHTML = roomname;
		document.getElementById("cstarttime").innerHTML = $("input[name=starttime]").val();
		document.getElementById("cendtime").innerHTML = $("input[name=endtime]").val();
		document.getElementById("cmembers").innerHTML = participants;
		document.getElementById("creason").innerHTML = reason;
		$("#commit").popup('open');
	}
	function submit() {
		var starttime = 0;
		var endtime = 0;
		// alert(pick1 + "," + pick2);
		if (chosen == 1) {
			starttime = endtime = pick1+pick2;
		} else {
			starttime = pick1<pick2?pick1:pick2;
			endtime = pick1>pick2?pick1:pick2;
		}
		// alert(starttime + "," + endtime);
		var reason = document.getElementById("reason").value+"";
		var participants = document.getElementById("members").value+"";
		var date1 = new Date();
		$.ajax({
			type: "post",
			dataType: "json",
			url: "/tspace/Servletapplication?op=submit",
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify({"idroom":idroom,"starttime":starttime,"endtime":endtime,"reason":reason,"participants":participants,"date":date1.getTime()}),
			success: function(data) {
				// alert("data="+data);
				if (data == -1) {
					// alert("亲，请登录");
					$("input[name=starttime]").val("");
						$("input[name=endtime]").val("");
					$("#popup1").popup('open');
					window.location.href="/tspace/User-mobile/login.jsp";
					return ;
				}
				if (data == -9) {
					return ;
				}
				if (data == -2) {
					// alert("申请提交出现错误！");
					$("input[name=starttime]").val("");
						$("input[name=endtime]").val("");
					$("#popup2").popup('open');
					window.location.reload();
					return ;
				}
				if (data == -3) {
					// alert("您在当天已有申请，请到我的申请中查看。");
					$("input[name=starttime]").val("");
					$("input[name=endtime]").val("");
					$("#popup3").popup('open');
					return ;
				}
				if (data == -4) {
					// 申请的时间已被占用 
					$("input[name=starttime]").val("");
					$("input[name=endtime]").val("");
					$("#popup4").popup('open');
					return ;
				}
				if (data == 0){
					// alert("提交成功，您的申请已提交到管理员处审核，审核结果将以短信方式通知您，也可在我的申请中查看");
					window.location.href="/tspace/User-mobile/applysuccess.jsp";
				}
			}
		});
	}