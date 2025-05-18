package co.ke.tucode.accounting.payloads;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ReceiptPayload {
    private String receiptNo;
    private LocalDate receiptDate;
    private String receivedFrom;
    private BigDecimal amount;
    private String amountInWords;
    private String paymentFor;
    private Long debitAccountId;
    private Long creditAccountId;
    private String paymentMethod;
    private String referenceNumber;
    private String description; // General description for the transaction(s)
}