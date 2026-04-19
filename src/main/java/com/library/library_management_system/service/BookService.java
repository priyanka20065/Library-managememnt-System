package com.library.library_management_system.service;
import com.library.library_management_system.entity.Book;
import com.library.library_management_system.entity.Member;
import com.library.library_management_system.entity.Transaction;
import com.library.library_management_system.repository.BookRepository;
import com.library.library_management_system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final TransactionRepository transactionRepository;
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        query, query, query, query
                );
    }
    @Transactional
    public boolean borrowBook(Book book, Member member) {
        if (book == null || member == null) return false;
        if (book.getTotalCopies() <= 0) return false;
        // Prevent double-borrowing or double-pending
        boolean alreadyRequested = transactionRepository.findByMember(member).stream()
                .anyMatch(t ->
                        t.getBook() != null &&
                        t.getBook().getId().equals(book.getId()) &&
                        (t.getStatus() == Transaction.Status.PENDING ||
                         t.getStatus() == Transaction.Status.PURCHASED) // ✅ kept as it is
                );
        if (alreadyRequested) return false;
        Transaction transaction = Transaction.builder()
            .book(book)
            .member(member)
            .purchaseDate(LocalDate.now())
            .status(Transaction.Status.PENDING)
            .build();
        // Decrement totalCopies on purchase
        book.setTotalCopies(book.getTotalCopies() - 1);
        transactionRepository.save(transaction);
        bookRepository.save(book);
        return true;
    }
    @Transactional
    public boolean returnBook(Book book, Member member) {
        if (book == null || member == null) return false;
        Optional<Transaction> transactionOpt = transactionRepository.findByMember(member).stream()
            .filter(t ->
                t.getBook() != null &&
                t.getBook().getId().equals(book.getId()) &&
                t.getStatus() == Transaction.Status.PURCHASED
            )
            .findFirst();
        if (transactionOpt.isEmpty()) return false;
        Transaction transaction = transactionOpt.get();
        transaction.setStatus(Transaction.Status.RETURNED);
        transaction.setReturnDate(LocalDate.now());
        transactionRepository.save(transaction);
        // Increase totalCopies on return
        book.setTotalCopies(book.getTotalCopies() + 1);
        bookRepository.save(book);
        return true;
    }
}