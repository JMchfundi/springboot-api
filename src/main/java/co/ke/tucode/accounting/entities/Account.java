package co.ke.tucode.accounting.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    
    @Enumerated(EnumType.STRING)
    private AccountType type;

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();
}