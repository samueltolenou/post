package com.formation.payload.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType;

}
