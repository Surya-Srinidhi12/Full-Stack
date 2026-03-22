<%@ page import="java.sql.*" %>
<%@ page import="dao.DBConnection" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<head>
<title>Events</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<div class="container">

    <div style="text-align:right;">
        <a href="ReportServlet" class="report-btn">Report</a>
        <a href="LogoutServlet" style="color:white; font-weight:bold;">Logout</a>
    </div>

    <h2>Available Events</h2>

    <!-- MESSAGE BLOCK -->
    <%
    String msg = request.getParameter("msg");

    if("closed".equals(msg)){
    %>
        <p style="color:red; text-align:center;">Registration Closed for this event!</p>
    <%
    }
    else if("already".equals(msg)){
    %>
        <p style="color:orange; text-align:center;">You already registered for this event!</p>
    <%
    }
    else if("success".equals(msg)){
    %>
        <p style="color:green; text-align:center;">Registration Successful!</p>
    <%
    }
    %>

    <div class="card-container">

    <%
    try {
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM events");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        while(rs.next()){
    %>

        <div class="card">
            <img src="<%= rs.getString("image") %>" alt="Event Image">
            <h3><%= rs.getString("name") %></h3>

            <%
                String formattedDate = "";
                try {
                    Date eventDate = rs.getDate("date");
                    if(eventDate != null){
                        formattedDate = sdf.format(eventDate);
                    } else {
                        formattedDate = "Date Not Available";
                    }
                } catch(Exception innerEx) {
                    formattedDate = rs.getString("date"); 
                }
            %>

            <p><b>Date:</b> <%= formattedDate %></p>
            <p><b>Venue:</b> <%= rs.getString("venue") %></p>
            <p><%= rs.getString("description") %></p>

            <form action="EventRegistrationServlet" method="post">
                <input type="hidden" name="eventId" value="<%= rs.getInt("id") %>">
                <button type="submit">Register</button>
            </form>
        </div>

    <%
        }

        rs.close();
        st.close();
        con.close();

    } catch(Exception e){
        out.println("<p style='color:red;text-align:center;'>Database Error</p>");
    }
    %>

    </div>
</div>

</body>
</html>