package com.tokio.marine.demo.controller;

import com.tokio.marine.demo.dto.TransferRequest;
import com.tokio.marine.demo.model.Transfer;
import com.tokio.marine.demo.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> schedule(@Valid @RequestBody TransferRequest request) {
        return transferService.schedule(request);
    }

    @GetMapping
    public ResponseEntity<Page<Transfer>> getAllTransfers(Pageable pageable) {
        return transferService.getAllScheduledTransfers(pageable);
    }
}
