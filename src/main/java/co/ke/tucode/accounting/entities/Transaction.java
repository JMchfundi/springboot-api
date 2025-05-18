package co.ke.tucode.accounting.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String description; // ✅ New field added

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEBIT or CREDIT

    @ManyToOne
    private Account account;

    @ManyToOne
    private JournalEntry journalEntry;
    
  // Added for extensible attributes
    @Column(columnDefinition = "TEXT") // Or your database's JSON type
    private String details; // Using String for now, consider JPA Converters

    // Added to handle two accounts
    private Long debitAccountId;
    private Long creditAccountId;

}
