import { useState } from "react";
import { login } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
    const [accountId, setAccountId] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const { token } = await login(accountId, password);
            localStorage.setItem("jwt", token);
            navigate("/chat-room-list");
        } catch (err:any) {
            alert("로그인 실패");
            alert(err.message());
        }
    };

    return (
        <div>
            <input value={accountId} onChange={(e) => setAccountId(e.target.value)} placeholder="아이디" />
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="비밀번호" />
            <button onClick={handleLogin}>로그인</button>
        </div>
    );
}
