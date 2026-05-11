import { useEffect, useState } from "react";
import API from "../api";
import { useNavigate } from "react-router-dom";
import ReactMarkdown from "react-markdown";
import "../styles/chat.css"

function Chat() {
  const [msg, setMsg] = useState("");
  const [chat, setChat] = useState([]);
  const navigate = useNavigate();

  async function loadHistory() {
    const res = await API.get("/chat/history");
    setChat(res.data);
  }

  useEffect(() => {
    loadHistory();
  }, []);

  async function send() {
    try {
      const res = await API.post("/chat", { message: msg });

      setChat([
        ...chat,
        { role: "USER", message: msg },
        { role: "AI", message: res.data }
      ]);

    } catch (err) {

      let errorMsg = "Something went wrong";

      if (err.response?.status === 429) {
        errorMsg = "⚠️ Too many requests. Please wait a few seconds.";
      }

      setChat([
        ...chat,
        { role: "USER", message: msg },
        { role: "AI", message: errorMsg }
      ]);
    }

    setMsg("");
  }

  function logout() {
    localStorage.clear();
    navigate("/");
  }

  return (
    <div id="chatAppContainer">

      <div id="chatAppHeader">
        <h3 id="chatUsername">{localStorage.getItem("username")}</h3>
        <button id="chatLogoutBtn" onClick={logout}>Logout</button>
      </div>

      <div id="chatMessagesContainer">
        {chat.map((c, i) => (
          <div
            key={i}
            className={`chatMessageRow ${c.role === "USER" ? "chatUserRow" : "chatAiRow"}`}
          >
            <div className="chatBubble">
              <ReactMarkdown>{c.message}</ReactMarkdown>
            </div>
          </div>
        ))}
      </div>

      <div id="chatInputContainer">
        <input
          id="chatInputField"
          value={msg}
          onChange={(e) => setMsg(e.target.value)}
          placeholder="Type your message..."
        />

        <button id="chatSendBtn" onClick={send}>Send</button>
      </div>

    </div>
  );
}

export default Chat;