package com.example.template.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginSuccessDto {
    private TokenResponseDto tokenResponseDto;
    private LoginSuccessMemberDto loginSuccessMemberDto;
}
