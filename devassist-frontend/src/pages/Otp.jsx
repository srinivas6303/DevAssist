import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import API from "../api";
import "../styles/auth.css";

function Otp() {
  const [otp, setOtp] = useState("");
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const [params] = useSearchParams();

  const type = params.get("type");
  const username = localStorage.getItem("username");

  // ✅ VERIFY OTP
  async function handleSubmit(e) {
    e.preventDefault();

    if (!otp || otp.length !== 6) {
      setMsg("⚠️ Enter valid 6-digit OTP");
      return;
    }

    try {
      let url =
        type === "login"
          ? "/auth/verify-login-otp"
          : "/auth/verify-register-otp";

      const res = await API.post(url, { username, otp });

      // ✅ LOGIN SUCCESS
      if (res.data.startsWith("ey")) {
        localStorage.setItem("token", res.data);
        navigate("/chat");
      }

      // ✅ REGISTER SUCCESS
      else if (res.data.toLowerCase().includes("success")) {
        alert("Registration Successful! Please login");
        navigate("/");
      }

      // ❌ ERROR
      else {
        setMsg(res.data);
      }

    } catch (err) {
      setMsg("❌ Something went wrong");
    }
  }

  // ✅ RESEND OTP (FIXED)
  async function resendOtp() {
  setLoading(true);
  setMsg("");

  try {
    let url =
      type === "login"
        ? "/auth/resend-login-otp"
        : "/auth/resend-register-otp";

    await API.post(url, { username });

    setMsg("✅ OTP resent successfully");

  } catch (err) {
    setMsg("❌ Failed to resend OTP");
  }

  setLoading(false);
}

  return (
    <div className="auth-wrapper">
      <form className="auth-card" onSubmit={handleSubmit}>
        
        <h2 className="auth-title">Enter OTP</h2>

        <p className="auth-subtext">
          OTP sent to <b>{username}</b>
        </p>

        <input
          className="auth-otp-input"
          maxLength="6"
          placeholder="Enter 6-digit OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
        />

        <button className="auth-btn" type="submit">
          Verify
        </button>

        {/* ✅ RESEND BUTTON */}
        <button
          type="button"
          className="auth-resend-btn"
          onClick={resendOtp}
          disabled={loading}
        >
          {loading ? "Resending..." : "Resend OTP"}
        </button>

        {/* ✅ MESSAGE */}
        {msg && <p className="auth-msg">{msg}</p>}

      </form>
    </div>
  );
}

export default Otp;