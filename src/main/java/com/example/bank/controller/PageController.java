package com.example.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	@GetMapping("/")
	public String test() {
		return "index";
	}
	
	@GetMapping("/loginForm")
	public String login() {
		return "login";
	}
	
	@GetMapping("/regForm")
	public String regist() {
		return "regist";
	}
	@GetMapping("/bank/main")
	public String bankMain() {
	    return "bank-main";
	}

	@GetMapping("/bank/deposit")
	public String depositPage() {
	    return "deposit";
	}

	@GetMapping("/bank/transfer")
	public String transferPage() {
	    return "transfer";
	}

	@GetMapping("/bank/transactions")
	public String transactionsPage() {
	    return "transactions";
	}
}
