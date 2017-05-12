<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="stu45" uri="http://cs.calstatela.edu/cs3220/stu45" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="scripts.js"></script>
<link rel="stylesheet" href="style.css"/>


<title>
<c:choose>
<c:when test = "${parentFile == null}">
MyFileManager
</c:when>

<c:otherwise>
${parentFile.name}
</c:otherwise>
</c:choose>

</title>
</head>
<body>

<header><h3><a href="MyFileManager">MyFileManager</a>
<span><a href="FileManagerLogout">Logout</a></span></h3>
</header>

<div class="main">
<div class="content">

<nav>
<div class="file-nav">
<c:if test="${parent == 0}">
<span>${user.fname}'s  Files</span>
</c:if>


<c:if test="${parent != 0}">
<c:choose>
<c:when test="${empty ancestors}">
<span><a href="MyFileManager"> ${user.fname}'s Files </a>&rarr;</span><span>${parentFile.name}</span>
</c:when>

<c:otherwise>
<a href="MyFileManager">${user.fname}'s Files</a>
<c:forEach items="${ancestors}" var="f">
<span><a href="MyFileManager?key=${f.id}">${f.name} </a>&rarr;</span>
</c:forEach>
<span>${parentFile.name}</span>
</c:otherwise>
</c:choose>
</c:if>
</div>
<div class="create">
<button id="createButton"><h4><img src="images/folder_add.ico"/><span>New Folder</span></h4></button>
<button id="uploadButton"><h4><img src="images/upload.ico"/><span> Upload File</span></h4></button>
<div id="createHidden" class="hidden">
<form  id="createForm">
	   File Name: <input type='text' name='name' />
	   <input type='submit' name='create' value='create' />
	   <input type='hidden' name='key' value='${parent}' />
        </form>
    </div>
    <div id="uploadHidden" class="hidden" >
    <form action="Upload?key=${parent}" method="post" enctype="multipart/form-data">
<input type="file" name="file1"/>
<input type="submit" name="upload" value="upload"/>

</form>
    </div>
</div>
</nav>
<table id="file-list">
<tr>
<th>Name</th>
<th>Date</th>
<th>Size</th>
<th>Operations</th>
</tr>




<c:if test="${not empty parentFile}">
<c:choose>
<c:when test="${not empty grand}">
<tr><td><img src="images/folder.ico"/><a href="MyFileManager?key=${grand.id}">\.....</a></td>
<td></td>
<td></td>
<td></td>
</tr>
</c:when>
<c:otherwise>
<tr><td><img src="images/folder.ico"/><a href="MyFileManager">\...</a></td>
<td></td>
<td></td>
<td></td>
</tr>
</c:otherwise>
</c:choose>
</c:if>

<tr id="shared-row">
<td><img src="images/shared.ico"/><a href="SharedFolder">Shared Folders</a></td>
<td></td>
<td></td>
<td></td>
</tr>

<c:forEach items="${Files}" var="f">
<tr>
<c:choose>
<c:when test="${not f.folder}">
<c:set var="icon" value="blank"/>
<c:set var="filename" value="${f.name}"/>
<c:set  var="ext" value='${fn:split(fn:replace(filename, ".", "|"), "|")}'/>

<c:forEach items="${imgLib}" var="l">
<c:if test="${ext[1] eq l }">
<c:set var="icon" value="${ext[1] }"/>
</c:if>

</c:forEach>


<td><img src="images/${icon}.ico"/><a href="Download?key=${f.id}&name=${f.name}&type=${f.type}">${f.name}</a></td> 
<td><fmt:formatDate pattern="MM/dd/yyyy hh:mma" value="${f.date}" /></td> 
<td>

<stu45:fileSize size="${f.size }"/>

</td>
<td><img src="images/edit.ico"/><a href="EditFolder?key=${f.id}">Rename</a> |
<img src="images/delete.ico"/><a href="#" class="delete" data-file="${f.id}">Delete</a> | 
<img src="images/share.ico"/><a href="#" class="share" data-file="${f.id}">Share</a>
</td>

</c:when>
<c:otherwise>
<td><img src="images/folder.ico"/><a href="MyFileManager?key=${f.id}">${f.name}</a></td> 
<td><fmt:formatDate pattern="MM/dd/yyyy hh:mma" value="${f.date}" /></td> 
<td>
</td>
<td><img src="images/folder_edit.ico"/><a href="EditFolder?key=${f.id}">Rename</a> | 
<img src="images/folder_delete.ico"/><a href="#" class="delete" data-file="${f.id}">Delete</a>

</td>



</c:otherwise>
</c:choose>

</tr>

</c:forEach>

</table>

<div id="dialog-box">
<p>Enter the user name of the user you'd like to share this file with</p>
<form id="share-form">
<input type="text" name="username"/>
<input type="submit"/>
</form>
</div>


</div>
<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>






</body>
</html>