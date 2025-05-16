package com.example.template.member.dto.request;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {
    private String accountId;
    private String password;
}
