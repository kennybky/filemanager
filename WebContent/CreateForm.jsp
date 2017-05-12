<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css"/>
<title>Create Folder</title>
</head>
<body>
<header><h3><a href="MyFileManager">MyFileManager</a></h3></header>
<div class="main">
<div class="content">
<div class="file-nav">
<form action="CreateFolder?key=${param.key}" method='post'>
	   File Name: <input type='text' name='name' />
	   <input type='submit' name='create' value='create' />
        </form>
</div>

</div>
<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>
</body>
</html>