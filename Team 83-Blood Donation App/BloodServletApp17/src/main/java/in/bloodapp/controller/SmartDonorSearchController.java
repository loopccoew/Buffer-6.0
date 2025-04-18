package in.bloodapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import in.bloodapp.utility.GraphSearchUtil;
import in.bloodapp.utility.jdbcUtil;

@WebServlet("/smartsearch")
public class SmartDonorSearchController extends HttpServlet {

    private static final Map<String, List<String>> bloodGroupMap = new HashMap<>();
    static {
        bloodGroupMap.put("A+", Arrays.asList("A+", "A-", "O+", "O-"));
        bloodGroupMap.put("A-", Arrays.asList("A-", "O-"));
        bloodGroupMap.put("B+", Arrays.asList("B+", "B-", "O+", "O-"));
        bloodGroupMap.put("B-", Arrays.asList("B-", "O-"));
        bloodGroupMap.put("AB+", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        bloodGroupMap.put("AB-", Arrays.asList("A-", "B-", "AB-", "O-"));
        bloodGroupMap.put("O+", Arrays.asList("O+", "O-"));
        bloodGroupMap.put("O-", Arrays.asList("O-"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bloodGroup = request.getParameter("blood_group");
        String pincode = request.getParameter("pincode");
        String urgency = request.getParameter("urgency_level");

        List<String> compatibleGroups = bloodGroupMap.getOrDefault(bloodGroup, new ArrayList<>());

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Smart Donor Search</title></head><body>");

        try (Connection conn = jdbcUtil.getDbConnection()) {
            String placeholders = String.join(",", Collections.nCopies(compatibleGroups.size(), "?"));
            String sql = String.format("SELECT * FROM donor WHERE blood_group IN (%s) AND pincode = ? ORDER BY %s",
                    placeholders,
                    urgency.equalsIgnoreCase("High") ? "last_donation_date ASC" :
                    urgency.equalsIgnoreCase("Medium") ? "age ASC" : "id ASC");

            PreparedStatement ps = conn.prepareStatement(sql);

            int index = 1;
            for (String bg : compatibleGroups) {
                ps.setString(index++, bg);
            }
            List<String> pincodes = GraphSearchUtil.getNearbyPincodes(pincode, 2);
            String pinPlaceholders = String.join(",", Collections.nCopies(pincodes.size(), "?"));

            sql = String.format("SELECT * FROM donor WHERE blood_group IN (%s) AND pincode IN (%s) ORDER BY %s",
                    placeholders,
                    pinPlaceholders,
                    urgency.equalsIgnoreCase("High") ? "last_donation_date ASC" :
                    urgency.equalsIgnoreCase("Medium") ? "age ASC" : "id ASC");

            ps = conn.prepareStatement(sql);

            // Set blood groups
            int idx = 1;
            for (String bg : compatibleGroups) ps.setString(idx++, bg);
            // Set nearby pincodes
            for (String pin : pincodes) ps.setString(idx++, pin);


            ResultSet rs = ps.executeQuery();

            out.println("<h2 style='text-align:center;'>Matching Donors</h2>");
            out.println("<table border='1' align='center'>");
            out.println("<tr><th>Name</th><th>Age</th><th>Blood Group</th><th>Address</th><th>Pincode</th><th>Last Donation</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getInt("age") + "</td>");
                out.println("<td>" + rs.getString("blood_group") + "</td>");
                out.println("<td>" + rs.getString("address") + "</td>");
                out.println("<td>" + rs.getString("pincode") + "</td>");
                out.println("<td>" + rs.getDate("last_donation_date") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='6' style='text-align:center;'>No matching donors found</td></tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
