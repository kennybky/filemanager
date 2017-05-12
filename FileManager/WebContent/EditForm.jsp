<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css"/>
<title>Edit ${value}</title>
</head>
<body>
<header><h3><a href="MyFileManager">MyFileManager</a>
<span><a href="FileManagerLogout">Logout</a></span></h3></header>
<div class="main">
<div class="content">
<div class="file-nav">
	<form action="EditFolder?key=${param.key}" method="post">
	    File Name: <input type='text' name='name' value="${value}" /> 
	 <input type='submit' name='edit' value='edit' /> 
       </form>
</div>

</div>
<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>
</body>
</html>