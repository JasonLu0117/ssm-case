<body>
<form action="user/login" method="get">
<ul>
 <li>username:<input type="text" name="username" /> </li>
 <li>password:<input type="text" name="password" /> </li>
 <li>role  id:<input type="text" name="roleId" /> </li>
 <li>code:<input type="text" name="code" /> <span><img id="img" src="http://localhost:8080/ssm-case//user/captcha.jpg" onclick="refresh()"></span></li>
 <li><input type="submit" value="login" /> </li>
</ul>
</form>
</body>

<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
<script>
	function refresh() {  
		console.log("-------");
	    var url = "http://localhost:8080/ssm-case/user/captcha.jpg?number="+Math.random();  
	    $("#img").attr("src",url);  
	}
</script>