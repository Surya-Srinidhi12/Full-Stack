package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import dao.DBConnection;

public class EventRegistrationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 🔐 Session safety
        if(session == null || session.getAttribute("studentId") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        int eventId = Integer.parseInt(request.getParameter("eventId"));
        int studentId = (int) session.getAttribute("studentId");

        try {
            Connection con = DBConnection.getConnection();

            // STEP 1: CHECK DEADLINE
            String checkDateSql = "SELECT last_date FROM events WHERE id=?";
            PreparedStatement datePs = con.prepareStatement(checkDateSql);
            datePs.setInt(1, eventId);
            ResultSet dateRs = datePs.executeQuery();

            if(dateRs.next()){
                java.sql.Date lastDate = dateRs.getDate("last_date");
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

                if(lastDate != null && today.after(lastDate)){
                    response.sendRedirect("dashboard.jsp?msg=closed");
                    return;
                }
            }

            // STEP 2: CHECK DUPLICATE
            String checkSql = "SELECT * FROM registrations WHERE student_id=? AND event_id=?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, studentId);
            checkStmt.setInt(2, eventId);

            ResultSet rs = checkStmt.executeQuery();

            if(rs.next()){
                response.sendRedirect("dashboard.jsp?msg=already");
                return;
            }

            // STEP 3: INSERT
            String sql = "INSERT INTO registrations(student_id, event_id) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);

            ps.executeUpdate();

            response.sendRedirect("dashboard.jsp?msg=success");

        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?msg=error");
        }
    }
}
