package com.sangwon97.portfolio.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    private LocalDate visitDate;

    @PrePersist
    public void prePersist() {
        if (visitDate == null) {
            visitDate = LocalDate.now();
        }
    }
}

