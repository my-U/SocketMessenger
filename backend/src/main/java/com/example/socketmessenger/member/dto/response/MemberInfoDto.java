package com.example.socketmessenger.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {

    private String accountId;
    private String role;
}
