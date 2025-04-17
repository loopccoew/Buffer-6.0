
  

document.addEventListener('DOMContentLoaded', () => {

    const form = document.getElementById('symptomForm');
    const resultDiv = document.getElementById('symptomResult');
  
    const highRiskSymptoms = ["bleeding", "dizziness", "abdominal pain", "swelling"];
  
     // Predefined Emergency Contacts
const emergencyContacts = [
    { name: "Dr. Neha Sharma", contact: "neha.sharma@healthcare.com" },
    { name: "Family Member - Mom", contact: "+91-9876543210" },
    { name: "Support Line", contact: "pregnancy-care@support.org" }
  ];


  // Notify Emergency Contact if high-risk symptoms found
function notifyEmergencyContacts() {
    emergencyContacts.forEach(contact => {
      console.log(`Notifying ${contact.name} at ${contact.contact}...`);
      alert(`🚨 Notification sent to ${contact.name} at ${contact.contact} about high-risk symptoms.`);
    });
  }

    form.addEventListener('submit', function (e) {
      e.preventDefault();
  
      const selected = Array.from(form.elements['symptom'])
        .filter(input => input.checked)
        .map(input => input.value);

        resultDiv.innerHTML = "";
  
      if (selected.length === 0) {
        resultDiv.innerHTML = "<p style='color:orange'>⚠️ Please select at least one symptom.</p>";
        return;
      }
  
      const critical = selected.filter(symptom => highRiskSymptoms.includes(symptom));
      const mild = selected.filter(symptom => !highRiskSymptoms.includes(symptom));
  
      let message = "";
  
      if (critical.length > 0) {
        message += `<p style="color:red">🚨 <strong>High-risk symptoms detected:</strong> ${critical.join(", ")}. Please seek medical attention immediately.</p>`;
        notifyEmergencyContacts();
      }
  
      if (mild.length > 0) {
        message += `<p style="color:green">✅ Mild symptoms noted: ${mild.join(", ")}. Please monitor and rest.</p>`;
      }
  
      resultDiv.innerHTML = message;

      // Save Emergency Contact
  

    });
  });
  