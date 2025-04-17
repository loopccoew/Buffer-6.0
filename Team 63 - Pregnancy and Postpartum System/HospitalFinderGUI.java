import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HospitalFinderGUI extends JFrame {

    private JTextField cityInput;
    private JTextArea resultArea;

    public HospitalFinderGUI() {
        setTitle("Nearest Hospital Finder");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel prompt = new JLabel("Enter City:");
        cityInput = new JTextField(20);
        JButton searchBtn = new JButton("Find Hospitals");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JPanel topPanel = new JPanel();
        topPanel.add(prompt);
        topPanel.add(cityInput);
        topPanel.add(searchBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> searchHospitals());
    }

    private void searchHospitals() {
        String userCity = cityInput.getText().trim().toLowerCase();
        double[] location = HospitalFinder.cityToLatLon.get(userCity);

        if (location == null) {
            resultArea.setText("City not recognized. Try: Pune, Mumbai, Delhi, Bangalore, Hyderabad, Chennai, Kolkata");
            return;
        }

        List<Hospital> hospitals = HospitalFinder.loadHospitals("large_hospital_dataset.csv");
        List<Hospital> nearest = HospitalFinder.getNearestHospitals(hospitals, location[0], location[1], 5);

        StringBuilder sb = new StringBuilder("Nearest Hospitals to " + userCity.toUpperCase() + ":\n");
        for (Hospital h : nearest) {
            double dist = h.distanceTo(location[0], location[1]);
            sb.append(String.format("- %s (%.2f km)\n", h.name, dist));
        }

        resultArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalFinderGUI().setVisible(true));
    }
}
