import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import HospitalGraph;

public class NearestHospitalsServlet extends HttpServlet {
    private HospitalGraph hospitalGraph;

    @Override
    public void init() throws ServletException {
        hospitalGraph = new HospitalGraph();

        // Add hospitals
        hospitalGraph.addHospital("City Hospital");
        hospitalGraph.addHospital("Green Cross");
        hospitalGraph.addHospital("Apollo Care");
        hospitalGraph.addHospital("Hope Medical");
        hospitalGraph.addHospital("Sunrise Clinic");
        hospitalGraph.addHospital("Fortis Heal");

        // Add edges (distances)
        hospitalGraph.addEdge("City Hospital", "Green Cross", 2.0);
        hospitalGraph.addEdge("City Hospital", "Apollo Care", 4.5);
        hospitalGraph.addEdge("Green Cross", "Hope Medical", 3.0);
        hospitalGraph.addEdge("Apollo Care", "Fortis Heal", 2.0);
        hospitalGraph.addEdge("Hope Medical", "Sunrise Clinic", 1.5);
        hospitalGraph.addEdge("Sunrise Clinic", "Fortis Heal", 2.5);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startHospital = request.getParameter("hospital");
        List<String> nearest = hospitalGraph.getNearestHospitals(startHospital, 5);

        JSONArray jsonArray = new JSONArray();
        for (String hospital : nearest) {
            JSONObject obj = new JSONObject();
            obj.put("name", hospital);
            jsonArray.put(obj);
        }

        response.setContentType("application/json");
        response.getWriter().print(jsonArray.toString());
    }
}
