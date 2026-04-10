package com.example.bank.dto;

import lombok.Data;

@Data
public class DepositDto {
	private String accountNumber;
	private Long amount;
}
