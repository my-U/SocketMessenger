package com.example.socketmessenger.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    public static String extractTokenFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            return root.path("token").asText(); // 없으면 "" 반환
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }
}