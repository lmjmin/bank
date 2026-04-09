package com.example.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class Account {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String accountNumber;
	
	@Column(nullable = false)
	private Long balance;
	
	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
	    ACTIVE, FROZEN
	}
}
