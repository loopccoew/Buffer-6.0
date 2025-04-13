import React, { useState } from "react";
import axios from "axios";
import { GoogleLogin } from "@react-oauth/google"; // Import the Google Login component
import "./Signup.css";

function Signup() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSignup = async (event) => {
    event.preventDefault(); // Prevent page reload
    const userCredentials = { username, password };

    setIsLoading(true); // Show loading state

    try {
      // Make API call to signup
      const response = await axios.post(
        "http://localhost:9191/api/auth/users/signup",
        userCredentials
      );

      // If successful, display success message
      if (response.data.message) {
        setMessage(response.data.message);
      } else {
        setMessage("User created successfully");
      }
    } catch (error) {
      console.log("Error during signup:", error);

      // Handle 409 conflict error (e.g. username/email already taken)
      if (error.response && error.response.status === 409) {
        setMessage("This username is already taken. Please try another one.");
      } else if (error.response) {
        setMessage(error.response.data.message); // Display backend error message
      } else {
        setMessage("An unexpected error occurred");
      }
    } finally {
      setIsLoading(false); // Reset loading state
    }
  };

  const handleGoogleSuccess = (response) => {
    const googleData = { token: response.credential };

    axios
      .post("http://localhost:9191/api/auth/google", googleData)
      .then((res) => {
        setMessage(res.data.message); // Handle success
      })
      .catch((err) => {
        setMessage("Error with Google signup");
      });
  };

  const handleGoogleFailure = (error) => {
    setMessage("Google login failed");
  };

  return (
    <div className="signup-container">
      <div className="signup-card">
        <h2>Signup</h2>
        <form onSubmit={handleSignup}>
          <div className="input-group">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="input-group">
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" disabled={isLoading}>
            {isLoading ? "Signing up..." : "Signup"}
          </button>
        </form>
        <div className="or-divider">OR</div>
        {/* Google Login */}
        <GoogleLogin
          onSuccess={handleGoogleSuccess}
          onError={handleGoogleFailure}
          useOneTap
          clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID} // Ensure you're using the environment variable for Google Client ID
        />
        <p>{message}</p> {/* Show success or error message */}
      </div>
    </div>
  );
}

export default Signup;
