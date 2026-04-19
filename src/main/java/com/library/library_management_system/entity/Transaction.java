package com.library.library_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate purchaseDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private Double fine;
    private double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String rejectionReason;

    public enum Status {
        PENDING, PURCHASED, RETURNED, REJECTED
    }
}
