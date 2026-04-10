package com.example.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.entity.Account;
import com.example.bank.entity.Member;

public interface AccountRepository extends JpaRepository<Account, Long>{
	Optional<Account> findByAccountNumber(String accountNumber); // 계좌번호 조회
	boolean existsByAccountNumber(String accountNumber); // 계좌번호 중복 체크
	Optional<Account> findByMemberUsername(String username); // 회원으로 계좌번호 조회
	Optional<Account> findByMember(Member member); // 회원 이름 찾기
}
