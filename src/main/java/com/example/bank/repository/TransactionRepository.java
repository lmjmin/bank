package com.example.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findTop5BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
		    Account sender, Account receiver
		); // 최근 거래 5개 
}
