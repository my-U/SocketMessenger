package com.example.socketmessenger.member.controller;

import com.example.socketmessenger.member.dto.request.MemberRegisterDto;
import com.example.socketmessenger.member.dto.response.MemberInfoDto;
import com.example.socketmessenger.member.service.MemberService;
import com.example.socketmessenger.util.ResponseUtil;
import com.example.socketmessenger.util.enums.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRegisterDto memberRegisterDto) {

        memberService.register(memberRegisterDto);
        return ResponseUtil.createSuccessResponse(SuccessCode.SIGNUP_SUCCESS);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(HttpServletRequest httpServletRequest) {

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(httpServletRequest);
        return ResponseUtil.createSuccessResponse(SuccessCode.SELECT_SUCCESS, memberInfoDto);
    }
}
