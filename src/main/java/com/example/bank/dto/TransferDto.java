package com.example.bank.dto;

import lombok.Data;

@Data
public class TransferDto {
	private String senderAccountNumber;
	private String receiverAccountNumber;
	private Long amount;
}
