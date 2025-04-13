import React from "react";
import { GoogleOAuthProvider } from "@react-oauth/google"; // Import the GoogleOAuthProvider
import Signup from "./components/Signup"; // Import your Signup component

function App() {
  return (
    <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}>
      <div className="App">
        <Signup />
      </div>
    </GoogleOAuthProvider>
  );
}

export default App;

// import React from "react";
// import { Routes, Route, Link } from "react-router-dom";
// // import Login from "./components/Login";
// import Signup from "./components/Signup";
// // import Dashboard from "./components/Dashboard";

// function App() {
//   return (
//     <div className="App">
//       <nav>
//         <ul>
//           {/* <li>
//             <Link to="/login">Login</Link>
//           </li> */}
//           <li>
//             <Link to="/signup">Signup</Link>
//           </li>
//           {/* <li>
//             <Link to="/dashboard">Dashboard</Link>
//           </li> */}
//         </ul>
//       </nav>
//       <Routes>
//         {/* <Route path="/login" element={<Login />} /> */}
//         <Route path="/signup" element={<Signup />} />
//         {/* <Route path="/dashboard" element={<Dashboard />} /> */}
//       </Routes>
//     </div>
//   );
// }

// export default App;
