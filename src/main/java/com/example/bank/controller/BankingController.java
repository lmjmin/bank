package com.example.bank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.dto.DepositDto;
import com.example.bank.dto.TransferDto;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.jwt.JwtUtil;
import com.example.bank.service.BankingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banking")
@RequiredArgsConstructor
public class BankingController {
	
	private final JwtUtil jwtUtil;
	private final BankingService bankingService;
//	내계좌 조회
	@GetMapping("/my-account")
	public Account getMyAccount(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
		    throw new RuntimeException("토큰 없음");
		}
		String token = header.substring(7);
		String username = jwtUtil.getUsername(token);
		return bankingService.getMyAccount(username);
	}
//	최근 거래 조회
	@GetMapping("/transactions")
	public List<Transaction> getTransation(HttpServletRequest request){
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
		    throw new RuntimeException("토큰 없음");
		}
		String token = header.substring(7);
		String username = jwtUtil.getUsername(token);
		Account account = bankingService.getMyAccount(username);
		return bankingService.getRecentTransactions(account);		
	}
//	입금
	@PostMapping("/deposit")
	public String deposit(@RequestBody DepositDto depositDto) {
		bankingService.deposit(depositDto);
		return "입금 되었습니다.";
	}
//	송금 
	@PostMapping("/transfer")
	public String transfer(@RequestBody TransferDto transferDto) {
		bankingService.transfer(transferDto);
		return "송금 성공";
	}
	
}
