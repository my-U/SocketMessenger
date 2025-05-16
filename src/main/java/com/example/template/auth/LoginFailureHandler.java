package com.example.template.auth;

import com.example.template.util.ResponseUtil;
import com.example.template.util.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Value("${media-type.json}")
    private String mediaTypeJson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(mediaTypeJson);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ResponseUtil.createErrorResponse(ErrorCode.UNAUTHORIZED, exception.getMessage()).getBody()
                )
        );
        response.getWriter().flush();
        response.getWriter().close();

        log.info("로그인 실패: " + exception.getMessage());
    }
}
