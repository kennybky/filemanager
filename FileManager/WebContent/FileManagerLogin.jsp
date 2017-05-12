<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css" type="text/css"/>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<title>Login</title>
</head>
<body>
<header><h3><a href="MyFileManager">MyFileManager</a>
<span><a href="FileUserRegister">Register</a></span></h3></header>

<div class="main">
<div class="content">
<div class="forms">

<section>
<h2>Login</h2>
<p class="err_message">${message }</p>
	
	<form action='FileManagerLogin' method='post'>
	<label for="username" >UserName</label>
	<div>
		
		<input type='text' name='username' id="username"/>
		</div>
		
		<label for="password" >Password </label>
		<div>
		<input type='password' name='password' id="password"/>
		</div>
		
		<div><input type='submit' name='submit'  value='login'/></div>
		</form>
</section>
	
	<section>
	<div>
	<p>Or you can register by clicking the button below</p>
	</div>
	<a href="FileUserRegister"><button>Register</button></a>
	</section>
	
</div>
</div>


<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>

</body>
</html>