package com.formation.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLoginResponse {
    private Boolean success;
    private String message;
    private List<LoginResponse> object;
    private HttpStatus statusCode;
}
