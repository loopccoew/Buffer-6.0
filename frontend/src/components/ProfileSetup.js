import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Signup.css";

function ProfileSetup() {
  const [name, setName] = useState("");
  const [dob, setDob] = useState("");
  const [mobile, setMobile] = useState("");
  const [profession, setProfession] = useState("");
  const [city, setCity] = useState("");
  const [area, setArea] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const areaOptions = {
    Pune: [
      "Swargate",
      "Karvenagar",
      "Vanaz",
      "Mayur Colony",
      "Deccan",
      "Nalstop",
      "Hinjewadi",
      "Cinchwad",
      "Shivajinagar",
      "Vadgaon",
    ],
    Nashik: [
      "Gangapur Road",
      "Nashik Road",
      "Mumbai Naka",
      "Anandvalli",
      "CIDCO-Satpur",
      "Meri-Mhasrul",
      "Adgaon",
    ],
    Mumbai: [
      "Vasai",
      "Panvel",
      "Vashi",
      "Ghatkopar",
      "Thane",
      "Kurla",
      "Andheri",
      "Bandra",
    ],
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!city || !area) {
      setErrorMessage("Please fill the location fields (City & Area).");
      return;
    }

    if (!/^\d{10}$/.test(mobile)) {
      setErrorMessage("Please enter a valid 10-digit mobile number.");
      return;
    }

    const profileData = { name, dob, mobile, profession, city, area };

    try {
      await axios.post("YOUR_BACKEND_API_URL_HERE", profileData);
      navigate("/home");
    } catch (error) {
      console.error("Error saving profile:", error);
      setErrorMessage("Failed to save profile. Please try again.");
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-card">
        <h2 style={{ textAlign: "center" }}>Profile Setup</h2>
        <form onSubmit={handleSubmit} className="profile-form">
          <div className="form-group">
            <label>Full Name *</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Date of Birth *</label>
            <input
              type="date"
              value={dob}
              onChange={(e) => setDob(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Mobile Number *</label>
            <input
              type="tel"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              required
              placeholder="Enter 10-digit mobile no"
            />
          </div>

          <div className="form-group">
            <label>Profession (Optional)</label>
            <input
              type="text"
              value={profession}
              onChange={(e) => setProfession(e.target.value)}
              placeholder="E.g. Student, Engineer, Doctor"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>City *</label>
              <select
                value={city}
                onChange={(e) => {
                  setCity(e.target.value);
                  setArea("");
                }}
                required
              >
                <option value="">Select City</option>
                <option value="Pune">Pune</option>
                <option value="Mumbai">Mumbai</option>
                <option value="Nashik">Nashik</option>
              </select>
            </div>

            <div className="form-group">
              <label>Area *</label>
              <select
                value={area}
                onChange={(e) => setArea(e.target.value)}
                required
                disabled={!city}
              >
                <option value="">Select Area</option>
                {city &&
                  areaOptions[city].map((a, i) => (
                    <option key={i} value={a}>
                      {a}
                    </option>
                  ))}
              </select>
            </div>
          </div>

          {errorMessage && <p className="error-message">{errorMessage}</p>}

          <button type="submit" className="continue-button">
            Continue
          </button>
        </form>
      </div>
    </div>
  );
}

export default ProfileSetup;
