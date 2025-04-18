package in.bloodapp.controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import in.bloodapp.utility.jdbcUtil;

@WebServlet("/search-donors")
public class DonorSearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String SQL_SELECT_MATCHING_DONORS =
        "SELECT id, name, age, blood_group, address FROM donor WHERE blood_group = ?";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String recipientBloodGroup = request.getParameter("bloodGroup");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = jdbcUtil.getDbConnection();
            pstmt = connection.prepareStatement(SQL_SELECT_MATCHING_DONORS);
            pstmt.setString(1, recipientBloodGroup);
            rs = pstmt.executeQuery();

            out.println("<html><head><title>Matching Donors</title></head><body>");
            out.println("<h2 style='text-align:center;'>Matching Donors for Blood Group: " + recipientBloodGroup + "</h2>");
            out.println("<table border='1' align='center'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Age</th><th>Blood Group</th><th>Address</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getInt("age") + "</td>");
                out.println("<td>" + rs.getString("blood_group") + "</td>");
                out.println("<td>" + rs.getString("address") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='5' style='text-align:center; color:red;'>No matching donors found</td></tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        } finally {
            try {
                jdbcUtil.closeResources(rs, pstmt, connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
