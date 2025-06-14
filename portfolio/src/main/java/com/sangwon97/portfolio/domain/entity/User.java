package com.sangwon97.portfolio.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    private String id; // 로그인 ID

    private String password;

    // Getter / Setter
}
