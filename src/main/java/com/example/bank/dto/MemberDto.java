package com.example.bank.dto;

import com.example.bank.entity.Member;

import lombok.Data;

@Data
public class MemberDto {
	private String username;
	private String password;
	private String name;
	private Member.Role role;
}
