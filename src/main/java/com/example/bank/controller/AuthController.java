package com.example.bank.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.dto.MemberDto;
import com.example.bank.entity.Member;
import com.example.bank.jwt.JwtUtil;
import com.example.bank.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final MemberService memberService;
    private final JwtUtil jwtUtil;
//	 회원 가입
    @PostMapping("/signup")
    public String signup(@RequestBody MemberDto memberDto) {
    	memberService.regist(memberDto);
    	
    	return "회원가입 성공";
    }
//    로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberDto memberDto, HttpServletResponse response){
    	Member member = memberService.findByUsername(memberDto.getUsername());
    	
    	if(!memberService.checkPassword( memberDto.getPassword(),member.getPassword())) {
    		throw new RuntimeException("비밀번호 틀림");
    	}
    	
    	String token = jwtUtil.generateToken(member.getUsername(), member.getRole().name());//enum 타입을 String 으로 바꿀려면 name사용
    	response.setHeader("Authorization", "Bearer "+token);
    	return ResponseEntity.ok(Map.of(
    		    "token", token,
    		    "message", "로그인 성공"
    		));
    }
    
    @GetMapping("/check")
    public String checkUsername(@RequestParam("username")String username) {
    	System.out.println("--------------------------------------------------------");
        if(memberService.existsByUsername(username)){
            return "이미 사용중인 아이디입니다.";
        }
        return "사용 가능한 아이디입니다.";
    }
	
	
}
