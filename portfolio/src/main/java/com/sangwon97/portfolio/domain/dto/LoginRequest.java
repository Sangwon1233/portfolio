package com.sangwon97.portfolio.domain.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

