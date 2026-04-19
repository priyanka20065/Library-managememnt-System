package com.library.library_management_system.controller;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import com.library.library_management_system.entity.Book;
import com.library.library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BookViewController {
    private final BookService bookService;

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("toast", "Book deleted successfully!");
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Book> bookOpt = bookService.getBookById(id);
        if (bookOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", "Book not found!");
            return "redirect:/books";
        }
        model.addAttribute("book", bookOpt.get());
        return "edit-book";
    }

    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute("book") Book book, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit-book";
        }
        book.setId(id);
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("toast", "Book updated successfully!");
        return "redirect:/books";
    }

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute("book") Book book, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-book";
        }
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("toast", "Book added successfully!");
        return "redirect:/books";
    }
}
