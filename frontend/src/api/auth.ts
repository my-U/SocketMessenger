import api from "./api";
import { ApiResponse } from "./types/response";
import { LoginData } from "./types/auth";


export const login = async (
    accountId: string,
    password: string
): Promise<ApiResponse<LoginData>> => {
    const res = await api.post<ApiResponse<LoginData>>("/auth/login", { accountId, password });
    return res.data;
};

export const register = async (accountId: string, password: string) => {
    const res = await api.post(`/member/register`, {
        accountId,
        password,
    });
    return res.data;
};

export const checkDuplicateId = async (
    accountId: string
): Promise<ApiResponse<boolean>> => {
    const res = await api.post<ApiResponse<boolean>>(
        `/auth/checkDuplicateId`,
        { accountId }
    );
    return res.data;
};
