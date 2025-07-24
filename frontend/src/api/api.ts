import axios from "axios";

const api = axios.create({
    baseURL: "http://192.168.75.114:8080", // 공통 prefix
});

// 요청마다 자동으로 JWT 토큰을 헤더에 붙임
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers = config.headers ?? {}; // headers가 undefined이면 빈 객체로 초기화
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;
