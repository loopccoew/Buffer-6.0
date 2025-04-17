package in.bloodapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import in.bloodapp.utility.jdbcUtil;

@WebServlet("/register-recipient")
public class RecipientController extends HttpServlet {
    private static final String SQL_INSERT_RECIPIENT =
        "INSERT INTO recipient (name, age, blood_group, address, contact_number, urgency_level, required_date, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String bloodGroup = request.getParameter("bloodGroup");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        String urgency = request.getParameter("urgency");
        String date = request.getParameter("date");
        String pincode = request.getParameter("pincode");

        try (Connection conn = jdbcUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_RECIPIENT)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, bloodGroup);
            pstmt.setString(4, address);
            pstmt.setString(5, contact);
            pstmt.setString(6, urgency);
            pstmt.setDate(7, java.sql.Date.valueOf(date));
            pstmt.setString(8, pincode);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("dashboard-recipient.html");
            } else {
                response.getWriter().println("Recipient registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}


