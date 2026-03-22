package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import dao.DBConnection;

public class ReportServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if(session == null){
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            /* ================= ADMIN VIEW ================= */
            if(session.getAttribute("admin") != null){

                request.setAttribute("isAdmin", true);
                request.setAttribute("title", "Admin Event Registration Summary");

                Map<String, List<String>> adminMap = new LinkedHashMap<>();

                String sql = "SELECT e.name AS event_name, s.name AS student_name " +
                             "FROM events e " +
                             "LEFT JOIN registrations r ON e.id = r.event_id " +
                             "LEFT JOIN students s ON r.student_id = s.id " +
                             "ORDER BY e.id";

                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    String eventName = rs.getString("event_name");
                    String studentName = rs.getString("student_name");

                    adminMap.putIfAbsent(eventName, new ArrayList<>());

                    if(studentName != null){
                        adminMap.get(eventName).add(studentName);
                    }
                }

                request.setAttribute("adminMap", adminMap);
            }

            /* ================= STUDENT VIEW ================= */
            else if(session.getAttribute("studentId") != null){

                request.setAttribute("isAdmin", false);
                request.setAttribute("title", "My Registered Events");

                List<String> studentEvents = new ArrayList<>();

                int studentId = (int) session.getAttribute("studentId");

                String sql = "SELECT e.name FROM registrations r " +
                             "JOIN events e ON r.event_id = e.id " +
                             "WHERE r.student_id = ?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, studentId);

                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    studentEvents.add(rs.getString("name"));
                }

                request.setAttribute("studentEvents", studentEvents);
            }

            else{
                response.sendRedirect("login.jsp");
                return;
            }

            RequestDispatcher rd = request.getRequestDispatcher("report.jsp");
            rd.forward(request, response);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
