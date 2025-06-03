package com.example.socketmessenger.auth.controller;

import com.example.socketmessenger.auth.dto.request.CheckDuplicateIdDto;
import com.example.socketmessenger.auth.dto.response.TokenResponseDto;
import com.example.socketmessenger.auth.service.AuthService;
import com.example.socketmessenger.util.ResponseUtil;
import com.example.socketmessenger.util.enums.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/checkDuplicateId")
    public ResponseEntity<?> checkDuplicateId(@RequestBody CheckDuplicateIdDto checkDuplicateIdDto) {
        boolean isDuplicateId = authService.checkDuplicateId(checkDuplicateIdDto);
        return ResponseUtil.createSuccessResponse(SuccessCode.SELECT_SUCCESS, isDuplicateId);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);  // 토큰 블랙리스트 등 처리
        return ResponseUtil.createSuccessResponse(SuccessCode.LOGOUT_SUCCESS);
    }

    @PostMapping("/token")
    public ResponseEntity<?> refresh(@CookieValue(value = "RefreshToken", required = false) String refreshToken) {
        TokenResponseDto tokenResponseDto = authService.refreshAccessToken(refreshToken);
        return ResponseUtil.createSuccessResponse(SuccessCode.REFRESH_TOKEN_SUCCESS, tokenResponseDto);
    }
}
