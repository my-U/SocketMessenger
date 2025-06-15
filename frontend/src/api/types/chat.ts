export interface ChatRoom {
    roomId: string;
    roomName: string;
}

export type ChatMessage = {
    sender: string;
    content: string;
};