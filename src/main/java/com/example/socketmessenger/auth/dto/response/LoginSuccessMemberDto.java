package com.example.socketmessenger.auth.dto.response;

import com.example.socketmessenger.util.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSuccessMemberDto {
    private String accountId;
    private Role role;
}