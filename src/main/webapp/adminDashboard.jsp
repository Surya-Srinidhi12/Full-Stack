<%@ page import="java.sql.*, java.util.*, dao.DBConnection" %>

<%
if(session.getAttribute("admin") == null && session.getAttribute("studentId") == null){
    response.sendRedirect("login.jsp");
    return;
}

Connection con = DBConnection.getConnection();

String sql;
PreparedStatement ps;

/* 🔐 If Admin → Show All */
if(session.getAttribute("admin") != null){

    sql = "SELECT e.name AS event_name, s.name AS student_name " +
          "FROM events e " +
          "LEFT JOIN registrations r ON e.id = r.event_id " +
          "LEFT JOIN students s ON r.student_id = s.id " +
          "ORDER BY e.id";

    ps = con.prepareStatement(sql);
}

/* 👨‍🎓 If Student → Show Only Their Events */
else {

    int studentId = (int) session.getAttribute("studentId");

    sql = "SELECT e.name AS event_name, s.name AS student_name " +
          "FROM events e " +
          "LEFT JOIN registrations r ON e.id = r.event_id " +
          "LEFT JOIN students s ON r.student_id = s.id " +
          "WHERE s.id = ? " +
          "ORDER BY e.id";

    ps = con.prepareStatement(sql);
    ps.setInt(1, studentId);
}

ResultSet rs = ps.executeQuery();

Map<String, List<String>> eventMap = new LinkedHashMap<>();

while(rs.next()){
    String eventName = rs.getString("event_name");
    String studentName = rs.getString("student_name");

    if(!eventMap.containsKey(eventName)){
        eventMap.put(eventName, new ArrayList<String>());
    }

    if(studentName != null){
        eventMap.get(eventName).add(studentName);
    }
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Event Registration Summary</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<h2 style="color:white; text-align:center; margin-top:30px;">
<%
    if(session.getAttribute("admin") != null){
        out.print("Admin Event Registration Summary");
    } else {
        out.print("My Registered Events");
    }
%>
</h2>

<div class="container">

<%
for(String event : eventMap.keySet()){
    List<String> students = eventMap.get(event);

    /* If student login and no registration, skip event */
    if(session.getAttribute("studentId") != null && students.size() == 0){
        continue;
    }
%>

<div class="card">
    <h3><%= event %></h3>
    <p><b>Total Registrations:</b> <%= students.size() %></p>

    <% if(students.size() > 0){ %>
        <ul style="text-align:left;">
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
%>

</div>

</body>
</html>