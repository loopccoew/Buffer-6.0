document.addEventListener('DOMContentLoaded', () => {
    const hospitalForm = document.getElementById('hospitalForm');
    const hospitalResult = document.getElementById('hospitalResult');
  
    hospitalForm.addEventListener('submit', function (e) {
      e.preventDefault();
      const location = document.getElementById('location').value.trim();
  
      if (!location) {
        alert("Please enter your location.");
        return;
      }
  
      
      const dummyHospitals = [
        { name: "City Hospital", distance: "2.3 km", safety: "High" },
        { name: "MotherCare Clinic", distance: "3.1 km", safety: "Medium" },
        { name: "Sunrise Health Center", distance: "4.0 km", safety: "High" }
      ];
  
      let output = "<h4>Nearest Hospitals:</h4><ul>";
      dummyHospitals.forEach(h => {
        output += `<li><strong>${h.name}</strong> – ${h.distance}, Safety: ${h.safety}</li>`;
      });
      output += "</ul>";
  
      hospitalResult.innerHTML = output;
    });
  });
  
