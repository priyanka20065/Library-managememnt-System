package com.library.library_management_system.controller;

import com.library.library_management_system.entity.Member;
import com.library.library_management_system.entity.Transaction;
import com.library.library_management_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/member/{memberId}")
    public List<Transaction> getTransactionsForMember(@PathVariable Long memberId) {
        Member member = new Member();
        member.setId(memberId);
        return transactionService.getTransactionsForMember(member);
    }
}
