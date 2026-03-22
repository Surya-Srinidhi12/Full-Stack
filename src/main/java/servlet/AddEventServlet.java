package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import dao.DBConnection;

public class AddEventServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String date = request.getParameter("date");
        String venue = request.getParameter("venue");
        String description = request.getParameter("description");

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO events(name, date, last_date, venue, description) VALUES(?,?,?,?,?)";

PreparedStatement ps = con.prepareStatement(sql);
ps.setString(1, request.getParameter("name"));
ps.setString(2, request.getParameter("date"));
ps.setString(3, request.getParameter("lastDate"));
ps.setString(4, request.getParameter("venue"));
ps.setString(5, request.getParameter("description"));

ps.executeUpdate();

            response.sendRedirect("admin.jsp");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
