import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ChatRoom, getChatRooms, createChatRoom } from "../api/chat";

export default function ChatRoomListPage() {
    const [rooms, setRooms] = useState<ChatRoom[]>([]);
    const [roomName, setRoomName] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        loadRooms();
    }, []);

    const loadRooms = async () => {
        try {
            const data = await getChatRooms();
            setRooms(data);
        } catch (err) {
            alert("채팅방 목록 불러오기 실패");
        }
    };

    const handleCreateRoom = async () => {
        if (!roomName.trim()) {
            alert("방 이름을 입력하세요");
            return;
        }

        try {
            const newRoom = await createChatRoom(roomName);

            await loadRooms(); // 채팅방 생성 후 리스트 갱신

            navigate(`/chat/${newRoom.roomId}`, {
                state: { roomName: roomName },
            });
        } catch (err) {
            alert("방 생성 실패");
        }
    };

    return (
        <div>
            <h2>채팅방 목록</h2>

            {rooms.length === 0 ? (
                <p style={{ color: "#888", marginTop: "12px" }}>
                    채팅방이 없습니다.
                </p>
            ) : (
                <ul>
                    {rooms.map((room) => (
                        <li key={room.roomId}>
                            <button onClick={() => navigate(`/chat/${room.roomId}`, { state: { roomName: room.roomName } })}>
                                {room.roomName}
                            </button>
                        </li>
                    ))}
                </ul>
            )}

            <div style={{ marginTop: "16px" }}>
                <input
                    value={roomName}
                    onChange={(e) => setRoomName(e.target.value)}
                    placeholder="방 이름 입력"
                />
                <button onClick={handleCreateRoom} style={{ marginLeft: "8px" }}>
                    방 생성
                </button>
            </div>
        </div>
    );
}
