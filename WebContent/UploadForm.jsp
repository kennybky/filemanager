
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css"/>
<title>Upload</title>
</head>
<body>
<header><h3><a href="MyFileManager">MyFileManager</a>
<span><a href="FileManagerLogout">Logout</a></span></h3></header>
<div class="main">
<div class="content">
<div class="file-nav">
<form action="Upload?key=${param.key}" method="post" enctype="multipart/form-data">
<input type="file" name="file1"/>
<input type="submit" name="upload" value="upload"/>

</form>
</div>

</div>
<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>

</body>
</html>