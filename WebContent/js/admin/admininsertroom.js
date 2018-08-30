 		function fsubmit() {
            var form = document.getElementById("form1");
            var formData = new FormData(form);
            var oReq = new XMLHttpRequest();
            oReq.onreadystatechange = function(){
              if(oReq.readyState == 4) {
                if(oReq.status == 200) {
                	/*
                    var json=JSON.parse(oReq.responseText);
                    var result = '';
                    // result += 'name=' + ret['name'] + '<br>';
                    // result += 'gender=' + ret['gender'] + '<br>';
                     result += '<img src="' + json['photo'] + '" width="100">';
                     $('#result').html(result);
                     */                
                     var b = oReq.responseText;
                     // alert("b="+b);
                     if (b == -5) {
                     	alert("提交数据格式错误");
                     	return ;
                     }
                     if (b == -2) {
                     	alert("数据添加失败");
                     }
                     if (b == 0) {
                     	alert("数据添加成功");
                     	return ;
                     }
                } else {
                	alert("网络连接出错");
                	return ;
                }
              }
            }
            oReq.open("POST","/tspace/Servletroom?op=insertroom");
            oReq.send(formData); 
            return false;
        } 
        
        function loadpreview(file){
	        var img = document.getElementById("preview");
	        var reader = new FileReader();
	        reader.onload = function(evt) {
	        	img.src = evt.target.result;
	        }
	        reader.readAsDataURL(file.files[0]);
        }