package servlet;

import dao.DBConnection;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM students WHERE email=? AND password=?"
            );

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // 🔥 Destroy old session first
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                // ✅ Create new session
                HttpSession session = request.getSession(true);
                session.setAttribute("studentId", rs.getInt("id"));

                response.sendRedirect("dashboard.jsp");

            } else {
                response.getWriter().println("Invalid Credentials");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
