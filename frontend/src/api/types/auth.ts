export interface LoginData {
    tokenResponseDto: TokenResponseDto;
    loginSuccessMemberDto: LoginSuccessMemberDto;
}

export interface TokenResponseDto {
    accessToken: string;
    tokenType: string;
}

export interface LoginSuccessMemberDto {
    accountId: string;
    role: string;
}

