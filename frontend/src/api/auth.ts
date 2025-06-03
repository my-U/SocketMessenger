import axios from "axios";

const BASE_URL = "http://localhost:8080";

export interface LoginResponse {
    token: string;
}

export const login = async (
    accountId: string,
    password: string
): Promise<LoginResponse> => {
    const res = await axios.post<LoginResponse>(
        `${BASE_URL}/auth/login`,
        { accountId, password }
    );
    return res.data;
};

export const register = async (accountId: string, password: string) => {
    const res = await axios.post(`${BASE_URL}/member/register`, {
        accountId,
        password,
    });
    return res.data;
};

export const checkDuplicateId = async (
    accountId: string
): Promise<boolean> => {
    const res = await axios.post<{ status: number; message: string; data: boolean }>(
        `${BASE_URL}/auth/checkDuplicateId`,
        { accountId }
    );
    return res.data.data;
};
