import axios from "axios";

const BASE_URL = "http://localhost:8080/chat/rooms";

export interface ChatRoom {
    roomId: string;
    roomName: string;
}

export const getChatRooms = async (): Promise<ChatRoom[]> => {
    const res = await axios.get<ChatRoom[]>(`${BASE_URL}/list`);
    return res.data;
};

export const createChatRoom = async (roomName: string): Promise<ChatRoom> => {
    const res = await axios.post<ChatRoom>(`${BASE_URL}/create`, null, {
        params: { roomName },
    });
    return res.data;
};
