import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api";
import "../styles/auth.css";

function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    const res = await API.post("/auth/register", { username, password });

    if (res.data.includes("OTP")) {
      localStorage.setItem("username", username);
      navigate("/otp?type=register");
    } else {
      alert(res.data);
    }
  }

  return (
    <div className="auth-wrapper">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h2 className="auth-title">Register</h2>

        <input
          className="auth-input"
          placeholder="Email"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          className="auth-input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button className="auth-btn">Register</button>
      </form>
    </div>
  );
}

export default Register;