<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css"/>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="scripts.js"></script>
<title>Register</title>
</head>
<body>
<header><h3><a href="MyFileManager">MyFileManager</a>
<span><a href="FileManagerLogin">Login</a></span></h3></header>

<div class="main">
<div class="content">
<div class="forms">

<section>
<h2>Register</h2>
<p class="err_message">${message }</p>

<form id="register" action='FileUserRegister' method='post'>



	<label for="username">User Name</label>
	<div><input type='text' name='username' id='username'/></div>
	
	<label for="password">Password</label>
	<div> <input type='password' name='password' id='password'/></div>
	
	<label for="fname"> First Name</label>
	<div><input type='text' name='fName' id='fname'/></div>
	
	<label for="lname">Last Name</label>
	<div><input type='text' name='lName' id='lname'/></div>
		
		<div><input type='submit' name='submit'  value='Register'/></div>

		
		</form>
		</section>
		<section>
		<div>
	Or you can login by clicking the button below
	</div>
	<a href="FileManagerLogin"><button>Login</button></a>
		</section>
		</div>
		</div>
		
		<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>

</body>
</html>