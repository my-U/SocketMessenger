import { useEffect, useState, useRef } from "react";
import {useLocation, useParams} from "react-router-dom";
import {getMemberInfo} from "../api/member";

const ChatRoomPage = () => {
    const { roomId } = useParams();
    const location = useLocation();
    const roomName = location.state?.roomName;
    const [accountId, setAccountId] = useState<string | null>(null);
    const [messages, setMessages] = useState<string[]>([]);
    const [input, setInput] = useState("");
    const socketRef = useRef<WebSocket | null>(null);

    useEffect(() => {
        const socket = new WebSocket("ws://localhost:9090/ws");

        socketRef.current = socket;

        socket.onopen = async () => {
            console.log("WebSocket 연결됨");

            try {
                const response = await getMemberInfo();
                const accountId = response.data.accountId;

                setAccountId(accountId);

                // 방 입장 명령 전송
                socket.send(`/join ${roomId} ${accountId}`);
            } catch (err) {
                console.error("회원 정보 조회 실패", err);
                socket.close();
            }
        };

        socket.onmessage = (event) => {
            setMessages((prev) => [...prev, event.data]);
        };

        socket.onerror = (err) => {
            console.error("WebSocket 에러", err);
        };

        socket.onclose = () => {
            console.log("WebSocket 종료");
        };

        return () => {
            socket.close();
        };
    }, [roomId]);

    const sendMessage = () => {
        if (!accountId) {
            alert("로그인 정보가 없습니다. 다시 로그인하세요.");
            return;
        }

        if (!input.trim()) {
            alert("메시지를 입력하세요.");
            return;
        }

        if (!socketRef.current || socketRef.current.readyState !== WebSocket.OPEN) {
            alert("채팅 서버와 연결되지 않았습니다.");
            return;
        }

        socketRef.current.send(
            JSON.stringify({
                type: "MESSAGE",
                roomId,
                sender: accountId,
                content: input,
            })
        );
        setInput("");
    };

    return (
        <div>
            <h2>{roomName}</h2>
            <div style={{ border: "1px solid #ccc", height: "300px", overflowY: "scroll" }}>
                {messages.map((msg, idx) => (
                    <div key={idx}>{msg}</div>
                ))}
            </div>
            <input
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button onClick={sendMessage}>전송</button>
        </div>
    );
};

export default ChatRoomPage;
