package in.bloodapp.utility;

import java.util.*;

public class BloodCompatibility {
    public static Map<String, List<String>> getCompatibleDonors() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("A+", Arrays.asList("A+", "A-", "O+", "O-"));
        map.put("A-", Arrays.asList("A-", "O-"));
        map.put("B+", Arrays.asList("B+", "B-", "O+", "O-"));
        map.put("B-", Arrays.asList("B-", "O-"));
        map.put("AB+", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        map.put("AB-", Arrays.asList("A-", "B-", "AB-", "O-"));
        map.put("O+", Arrays.asList("O+", "O-"));
        map.put("O-", Arrays.asList("O-"));
        return map;
    }
}
