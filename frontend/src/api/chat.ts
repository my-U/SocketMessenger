import api from "./api";
import { ApiResponse } from "./types/response";
import { ChatRoom } from "./types/chat";

export const getChatRooms = async (): Promise<ChatRoom[]> => {
    const res = await api.get<ApiResponse<ChatRoom[]>>("/chat/rooms/list");
    return res.data.data; // 여기가 배열이어야 함
};

export const createChatRoom = async (roomName: string): Promise<ChatRoom> => {
    const res = await api.post<ApiResponse<ChatRoom>>(`/chat/rooms/create`, null, {
        params: { roomName },
    });
    return res.data.data;
};
