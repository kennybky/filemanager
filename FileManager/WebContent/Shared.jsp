<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="stu45" uri="http://cs.calstatela.edu/cs3220/stu45" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="style.css"/>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="scripts.js"></script>
<title>

Shared Files
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
<span>${user.fname}'s  Shared Files</span>
</div>

<div class="create">
<a href="MyFileManager">Back to Home</a>
</div>
</nav>


<table>
<tr>
<th>Name</th>
<th>Share Date</th>
<th>Shared By</th>
<th>Size</th>
<th>Operations</th>

</tr>





<c:forEach items="${Files}" var="f">
<tr>

<c:set var="icon" value="blank"/>
<c:set var="filename" value="${f.name}"/>
<c:set  var="ext" value='${fn:split(fn:replace(filename, ".", "|"), "|")}'/>

<c:forEach items="${imgLib}" var="l">
<c:if test="${ext[1] eq l }">
<c:set var="icon" value="${ext[1] }"/>
</c:if>

</c:forEach>


<td><img src="images/${icon}.ico"/><a href="Download?key=${f.file}&name=${f.name}&type=${f.type}&shared=${f.id}">${f.name}</a></td> 
<td><fmt:formatDate pattern="MM/dd/yyyy hh:mma" value="${f.date}" /></td> 
<td> ${f.ownername } </td>

<td>

<stu45:fileSize size="${f.size }"/>

</td>
<td><img src="images/delete.ico"/><a href="#" class="DeleteShared" data-file="${f.id}">Delete</a></td>




</tr>
</c:forEach>

</table>


</div>
<footer><div> Copyright &copy; 2017 Adekola Togunloju All Rights Reserved</div></footer>
</div>




</body>
</html>