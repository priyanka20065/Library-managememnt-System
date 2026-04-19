package com.library.library_management_system.service;

import com.library.library_management_system.entity.Member;
import com.library.library_management_system.entity.Transaction;
import com.library.library_management_system.entity.Book;
import com.library.library_management_system.repository.TransactionRepository;
import com.library.library_management_system.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsForMember(Member member) {
        return transactionRepository.findByMember(member);
    }

    public boolean rejectTransaction(Long transactionId, String rejectionReason) {
        var opt = transactionRepository.findById(transactionId);
        if (opt.isEmpty()) return false;
        Transaction transaction = opt.get();
        if (transaction.getStatus() != Transaction.Status.PENDING) return false;
        transaction.setStatus(Transaction.Status.REJECTED); // Mark as rejected
        transaction.setRejectionReason(rejectionReason);
        transactionRepository.save(transaction);
        return true;
    }

    public boolean approveTransaction(Long transactionId) {
        var opt = transactionRepository.findById(transactionId);
        if (opt.isEmpty()) return false;
        Transaction transaction = opt.get();
        if (transaction.getStatus() != Transaction.Status.PENDING) return false;
        // Set as PURCHASED, set purchaseDate, update book copies, set due date (2 weeks from now)
        transaction.setStatus(Transaction.Status.PURCHASED);
        transaction.setPurchaseDate(java.time.LocalDate.now());
        transaction.setDueDate(java.time.LocalDate.now().plusDays(14));
        transaction.setFine(0.0);
        var book = transaction.getBook();
        if (book.getAvailableCopies() <= 0) return false;
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        transactionRepository.save(transaction);
        bookRepository.save(book);
        return true;
    }

    public boolean returnBookByUser(Long transactionId) {
        var opt = transactionRepository.findById(transactionId);
        if (opt.isEmpty()) return false;
        Transaction transaction = opt.get();
        if (transaction.getStatus() != Transaction.Status.PURCHASED) return false;
        transaction.setStatus(Transaction.Status.RETURNED);
        transaction.setReturnDate(java.time.LocalDate.now());
        // Calculate fine if returned after due date (Rs. 25 per day late)
        if (transaction.getDueDate() != null && transaction.getReturnDate().isAfter(transaction.getDueDate())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(transaction.getDueDate(), transaction.getReturnDate());
            double fine = daysLate * 25.0; // Rs. 25 per day late
            transaction.setFine(fine);
        } else {
            transaction.setFine(0.0);
        }
        // Increase book available copies
        var book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        transactionRepository.save(transaction);
        bookRepository.save(book);
        return true;
    }
}
