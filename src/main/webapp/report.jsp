<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Report</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<h2 style="color:white; text-align:center;">
    <%= request.getAttribute("title") %>
</h2>

<%
Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");

/* ====================== ADMIN VIEW ====================== */
if(isAdmin != null && isAdmin){

    Map<String, List<String>> adminMap =
        (Map<String, List<String>>) request.getAttribute("adminMap");

    if(adminMap == null || adminMap.isEmpty()){
%>

        <p style="color:white; text-align:center;">
            No registrations available.
        </p>

<%
    } else {

        for(String event : adminMap.keySet()){
            List<String> students = adminMap.get(event);
%>

<div class="card">
    <h3><%= event %></h3>
    <p><b>Total Registrations:</b> <%= students.size() %></p>

    <% if(students.size() > 0){ %>
        <ul>
        <% for(String student : students){ %>
            <li><%= student %></li>
        <% } %>
        </ul>
    <% } else { %>
        <p>No registrations yet.</p>
    <% } %>
</div>

<%
        }
    }
}

/* ====================== STUDENT VIEW ====================== */
else {

    List<String> studentEvents =
        (List<String>) request.getAttribute("studentEvents");

    if(studentEvents == null || studentEvents.isEmpty()){
%>

        <p style="color:white; text-align:center;">
            You have not registered for any events.
        </p>

<%
    } else {

        for(String event : studentEvents){
%>

<div class="card">
    <h3><%= event %></h3>
    <p style="color:green;"><b>Registered Successfully</b></p>
</div>

<%
        }
    }
}
%>

</body>
</html>