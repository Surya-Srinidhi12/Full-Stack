package servlet;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
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
                session.setAttribute("admin", username);

                response.sendRedirect("adminDashboard.jsp");

            } else {
                response.sendRedirect("admin.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
