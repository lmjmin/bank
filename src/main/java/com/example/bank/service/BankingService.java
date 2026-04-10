package com.example.bank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bank.dto.DepositDto;
import com.example.bank.dto.TransferDto;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.Transaction.TransactionType;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.MemberRepository;
import com.example.bank.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    // 입금
    public void deposit(DepositDto deposit) {
        // 계좌 조회
    	Account account = accountRepository.findByAccountNumber
    			(deposit.getAccountNumber()).orElseThrow(() -> new RuntimeException("계좌 번호가 없습니다."));
        // 예외 검사
    	if(deposit.getAmount()<=0) {
    		throw new RuntimeException("금액이 올바르지 않습니다.");
    	}
    	if(account.getStatus() == Account.Status.FROZEN) {
    		throw new RuntimeException("동결된 계좌 입니다.");
    	}
        // 잔액 증가
        Long sumamount = account.getBalance() + deposit.getAmount();
        account.setBalance(sumamount);
    	// 거래 저장
        Transaction transaction = new Transaction();
        transaction.setReceiverAccount(account);
        transaction.setAmount(deposit.getAmount());
        transaction.setType(Transaction.TransactionType.DEPOSIT);
        
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

//    송금
    @Transactional
    public void transfer(TransferDto transferDto) {
        // 보내는 계좌 조회
    	Account senderAccount = accountRepository.findByAccountNumber
    			(transferDto.getSenderAccountNumber()).orElseThrow(() -> new RuntimeException("계좌번호 없음"));
        // 받는 계좌 조회
    	Account receiverAccount = accountRepository.findByAccountNumber
    			(transferDto.getReceiverAccountNumber()).orElseThrow(() -> new RuntimeException("계좌번호 없음"));
        // 예외 검사
    	if(transferDto.getAmount() <= 0) {
    		throw new RuntimeException("금액이 올바르지 않습니다.");
    	}
    	if (senderAccount.getBalance() < transferDto.getAmount()) {
    	    throw new RuntimeException("잔액 부족");
    	}
    	if(senderAccount.getStatus() == Account.Status.FROZEN || receiverAccount.getStatus() == Account.Status.FROZEN) {
    		throw new RuntimeException("동결된 계좌");
    	}
    	if (transferDto.getSenderAccountNumber().equals(transferDto.getReceiverAccountNumber())) {
    	    throw new RuntimeException("자기 자신에게 송금할 수 없습니다.");
    	}
        // 차감 / 증가
    	Long senderbalance = senderAccount.getBalance() - transferDto.getAmount();
    	senderAccount.setBalance(senderbalance);
    	Long receiverbalance = receiverAccount.getBalance() + transferDto.getAmount();
    	receiverAccount.setBalance(receiverbalance);
        // 거래 저장
    	Transaction transaction = new Transaction();
    	transaction.setSenderAccount(senderAccount);
    	transaction.setReceiverAccount(receiverAccount);
    	transaction.setAmount(transferDto.getAmount());
    	transaction.setType(TransactionType.TRANSFER);
    	
    	transactionRepository.save(transaction);
    	accountRepository.save(senderAccount);
    	accountRepository.save(receiverAccount);
    }

    public Account getMyAccount(String username) {
        // member 찾기
    	Member member = memberRepository.findByUsername(username)
    		    .orElseThrow(() -> new RuntimeException("회원 없음"));
    	// account 찾기
    	Account account = accountRepository.findByMember(member)
    		    .orElseThrow(() -> new RuntimeException("계좌 없음"));

    	return account;
        
    }

    public List<Transaction> getRecentTransactions(Account account) {
        return transactionRepository
            .findTop5BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(account, account);
    }
}
