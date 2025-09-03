package com.tokio.marine.demo.repository;

import com.tokio.marine.demo.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Page<Transfer> findAllByOrderByTransferDateAsc(Pageable pageable);
}
