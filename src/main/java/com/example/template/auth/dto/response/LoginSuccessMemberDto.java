package com.example.template.auth.dto.response;

import com.example.template.util.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSuccessMemberDto {
    private String accountId;
    private Role role;
}