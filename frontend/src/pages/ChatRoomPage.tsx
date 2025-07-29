import { useEffect, useState, useRef } from "react";
import {useLocation, useParams} from "react-router-dom";
import {getMemberInfo} from "../api/member";
import { ChatMessage } from "../api/types/chat";
import './ChatRoomPage.css';

const ChatRoomPage = () => {
    const { roomId } = useParams();
    const location = useLocation();
    const roomName = location.state?.roomName;
    const [accountId, setAccountId] = useState<string | null>(null);
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [input, setInput] = useState("");
    const socketRef = useRef<WebSocket | null>(null);
    const endRef = useRef<HTMLDivElement | null>(null);

    const isConnectedRef = useRef(false);

    useEffect(() => {

        if (isConnectedRef.current) {
            console.log("이미 연결된 상태");
            return;
        }

        const socket = new WebSocket("ws://52.79.209.9:9090/ws");
        socketRef.current = socket;
        isConnectedRef.current = true;


        socket.onopen = async () => {
            console.log("WebSocket 연결됨");

            try {
                const response = await getMemberInfo();
                const accountId = response.data.accountId;
                setAccountId(accountId);
                socket.send(`/join ${roomId} ${accountId}`);
            } catch (err) {
                console.error("회원 정보 조회 실패", err);
                socket.close();
            }
        };

        socket.onmessage = (event) => {
            try {
                const parsed: ChatMessage = JSON.parse(event.data);
                setMessages((prev) => [...prev, parsed]);
            } catch (e) {
                console.error("메시지 파싱 실패", e);
            }
        };

        socket.onerror = (err) => {
            console.error("WebSocket 에러", err);
        };

        socket.onclose = () => {
            console.log("WebSocket 종료");
            isConnectedRef.current = false;
        };

        return () => {
            socket.close();
            isConnectedRef.current = false;
        };
    }, [roomId]);


    useEffect(() => {
        endRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

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
        <div className="chat-room-container">
            <h2 className="room-title">{roomName}</h2>
            <div className="chat-box">
                {messages.map((msg, idx) => {
                    const isMine = msg.sender === accountId;
                    return (
                        <div
                            key={idx}
                            className={`chat-message ${isMine ? 'mine' : 'theirs'}`}
                        >
                            {msg.content}
                        </div>
                    );
                })}
                <div ref={endRef} />
            </div>
            <input
                className="chat-input"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button className="chat-send-button" onClick={sendMessage}>전송</button>
        </div>
    );
};

export default ChatRoomPage;
