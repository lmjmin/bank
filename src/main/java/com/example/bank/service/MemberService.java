package com.example.bank.service;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.dto.MemberDto;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.entity.Member.Role;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member regist(MemberDto memberDto) {

        // 1. 중복 아이디 검사
        if (memberRepository.findByUsername(memberDto.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 2. 회원 생성
        Member member = new Member();
        member.setUsername(memberDto.getUsername());

        String encodedPassword = bCryptPasswordEncoder.encode(memberDto.getPassword());
        member.setPassword(encodedPassword);

        member.setName(memberDto.getName());
        member.setRole(Role.USER);

        // 3. 회원 저장
        Member savedMember = memberRepository.save(member);

        // 4. 기본 계좌 생성
        Account account = new Account();
        account.setAccountNumber(createAccountNumber());
        account.setBalance(0L);
        account.setMember(savedMember);

        // 계좌 동결 상태 필드 
        account.setStatus(Account.Status.ACTIVE);

        // 5. 계좌 저장
        accountRepository.save(account);

        return savedMember;
    }

    private String createAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
    
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }
    
    public boolean checkPassword(String raw, String encoded) {
       return bCryptPasswordEncoder.matches(raw, encoded);
    }
    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}