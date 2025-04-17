package in.bloodapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import in.bloodapp.utility.jdbcUtil;

@WebServlet("/register-donor")
public class DonorController extends HttpServlet {
    private static final String SQL_INSERT_DONOR =
        "INSERT INTO donor (name, age, blood_group, address, pincode, last_donation_date) VALUES (?, ?, ?, ?, ?, ?)";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String bloodGroup = request.getParameter("bloodGroup");
        String address = request.getParameter("address");
        String pincode = request.getParameter("pincode");
        String lastDonationDate = request.getParameter("last_donation_date");

        try (Connection conn = jdbcUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_DONOR)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, bloodGroup);
            pstmt.setString(4, address);
            pstmt.setString(5, pincode);
            pstmt.setDate(6, java.sql.Date.valueOf(lastDonationDate));

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("dashboard-donor.html");
            } else {
                response.getWriter().println("Donor registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}
