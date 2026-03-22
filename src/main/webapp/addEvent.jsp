<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%
if(session.getAttribute("admin") == null){
    response.sendRedirect("admin.jsp");
    return;
}
%>

<html>
<head>
<title>Add Event</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h2>Add Event</h2>
    <form action="AddEventServlet" method="post">
        Title: <input type="text" name="title"><br>
        Description: <input type="text" name="description"><br>
        Date: <input type="date" name="date"><br>
        <input type="submit" value="Add Event">
        <input type="date" name="lastDate" required><br>
    </form>
</div>
</body>
</html>