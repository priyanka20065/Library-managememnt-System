package com.library.library_management_system.controller;

import com.library.library_management_system.entity.Book;
import com.library.library_management_system.entity.Member;
import com.library.library_management_system.service.BookService;
import com.library.library_management_system.service.MemberService;
import com.library.library_management_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final BookService bookService;
    private final TransactionService transactionService;
    private final MemberService memberService;

    @PostMapping("/transactions/return/{id}")
    public String returnBookByUser(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("toast", "You must be logged in to return a book.");
            return "redirect:/transactions";
        }

        boolean success = transactionService.returnBookByUser(id);

        model.addAttribute("toast",
                success ? "Book returned successfully." : "Return failed. Check transaction status.");

        return "redirect:/transactions";
    }

    @PostMapping("/transactions/reject/{id}")
    public String rejectTransaction(@PathVariable Long id, @RequestParam("reason") String reason, Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            model.addAttribute("toast", "Only admins can reject transactions.");
            return "redirect:/transactions";
        }

        boolean success = transactionService.rejectTransaction(id, reason);

        model.addAttribute("toast",
                success ? "Transaction rejected." : "Rejection failed. Check status.");

        return "redirect:/transactions";
    }

    @PostMapping("/transactions/approve/{id}")
    public String approveTransaction(@PathVariable Long id, Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            model.addAttribute("toast", "Only admins can approve transactions.");
            return "redirect:/transactions";
        }

        boolean success = transactionService.approveTransaction(id);

        model.addAttribute("toast",
                success ? "Transaction approved." : "Approval failed. Check availability or status.");

        return "redirect:/transactions";
    }

    @GetMapping("/books/borrow/{id}")
    public String borrowBook(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("toast", "You must be logged in to borrow a book.");
            return "redirect:/login";
        }

        String username = authentication.getName();
        Member member = memberService.getByUsername(username).orElse(null);
        Book book = bookService.getBookById(id).orElse(null);

        if (member == null || book == null) {
            model.addAttribute("toast", "Invalid book or user.");
            return "redirect:/books";
        }

        boolean success = bookService.borrowBook(book, member);

        model.addAttribute("toast",
                success ? "Book borrowed successfully!" :
                        "Unable to borrow book. Already borrowed or unavailable.");

        return "redirect:/books";
    }

    @GetMapping("/books")
    public String books(Model model, Authentication authentication) {
        return booksSearch(null, null, model, authentication);
    }

    @GetMapping("/books/search")
    public String booksSearch(@RequestParam(required = false) String query,
                              @RequestParam(required = false) String category,
                              Model model, Authentication authentication) {

        boolean isAdmin = isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);

        if ((query == null || query.isEmpty()) && (category == null || category.isEmpty())) {
            model.addAttribute("books", bookService.getAllBooks());

        } else if (category != null && !category.isEmpty()) {

            if (query != null && !query.isEmpty()) {
                model.addAttribute("books", bookService.getAllBooks().stream()
                        .filter(b -> b.getCategory() != null &&
                                b.getCategory().equalsIgnoreCase(category) &&
                                (b.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                 b.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                                 b.getIsbn().toLowerCase().contains(query.toLowerCase())))
                        .toList());
            } else {
                model.addAttribute("books", bookService.getAllBooks().stream()
                        .filter(b -> b.getCategory() != null &&
                                b.getCategory().equalsIgnoreCase(category))
                        .toList());
            }

        } else {
            model.addAttribute("books", bookService.searchBooks(query));
        }

        return "books";
    }

    @GetMapping("/transactions")
    public String transactions(Model model, Authentication authentication) {
        try {
            boolean isAdmin = isAdmin(authentication);
            model.addAttribute("isAdmin", isAdmin);

            String username = authentication != null ? authentication.getName() : null;
            Member member = username != null
                    ? memberService.getByUsername(username).orElse(null)
                    : null;

            if (isAdmin) {
                model.addAttribute("transactions", transactionService.getAllTransactions());
            } else if (member != null) {
                model.addAttribute("transactions", transactionService.getTransactionsForMember(member));
            } else {
                model.addAttribute("transactions", java.util.Collections.emptyList());
            }
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "An error occurred while loading transactions: " + ex.getMessage());
            model.addAttribute("transactions", java.util.Collections.emptyList());
        }
        return "transactions";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // ✅ Helper method (cleaner admin check)
    private boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        String username = authentication.getName();
        return memberService.getByUsername(username)
                .map(m -> m.getRole() == Member.Role.ADMIN)
                .orElse(false);
    }
}