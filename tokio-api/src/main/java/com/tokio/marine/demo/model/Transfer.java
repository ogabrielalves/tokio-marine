package com.tokio.marine.demo.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String sourceAccount;

    @Column(nullable = false, length = 20)
    private String destinationAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false)
    private LocalDate schedulingDate;
}
