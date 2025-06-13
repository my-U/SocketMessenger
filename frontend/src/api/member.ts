import api from "./api";
import { ApiResponse } from "./types/response";
import { MemberInfo } from "./types/member";

export const getMemberInfo = async (): Promise<ApiResponse<MemberInfo>> => {
    const res = await api.post<ApiResponse<MemberInfo>>("/member/info");
    return res.data;
};