package com.tokio.marine.demo.service;

import com.tokio.marine.demo.dto.TransferRequest;
import com.tokio.marine.demo.model.Transfer;
import com.tokio.marine.demo.repository.TransferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public ResponseEntity<String> schedule(TransferRequest request) {
        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, request.getTransferDate());

        BigDecimal fee = calculateFee(request.getAmount(), days);

        if (fee == null) {
            return ResponseEntity.badRequest().body("There is no fee applicable for this appointment. Transfer not scheduled.");
        }

        Transfer newTransfer = Transfer.builder()
                .sourceAccount(request.getSourceAccount())
                .destinationAccount(request.getDestinationAccount())
                .amount(request.getAmount())
                .fee(fee)
                .transferDate(request.getTransferDate())
                .schedulingDate(today)
                .build();

        transferRepository.save(newTransfer);

        return ResponseEntity.ok().body("Transfer scheduled successfully");
    }

    public ResponseEntity<Page<Transfer>> getAllScheduledTransfers(Pageable pageable) {
        Page<Transfer> transfers = transferRepository.findAllByOrderByTransferDateAsc(pageable);

        if (transfers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transfers);
        }
    }

    private BigDecimal calculateFee(BigDecimal amount, long days) {
        if (days < 0) return null;

        if (days == 0) {
            return BigDecimal.valueOf(3.0).add(amount.multiply(BigDecimal.valueOf(0.025)));
        } else if (days <= 10) {
            return BigDecimal.valueOf(12.0);
        } else if (days <= 20) {
            return amount.multiply(BigDecimal.valueOf(0.082));
        } else if (days <= 30) {
            return amount.multiply(BigDecimal.valueOf(0.069));
        } else if (days <= 40) {
            return amount.multiply(BigDecimal.valueOf(0.047));
        } else if (days <= 50) {
            return amount.multiply(BigDecimal.valueOf(0.017));
        }

        return null;
    }
}
