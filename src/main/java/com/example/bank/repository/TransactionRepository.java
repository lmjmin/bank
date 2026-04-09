package com.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
