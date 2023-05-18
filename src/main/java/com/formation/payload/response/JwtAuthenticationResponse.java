package com.formation.payload.response;

public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private boolean hasDefaultPassword=false;
    private String changePasswordToken="";

    public JwtAuthenticationResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthenticationResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isHasDefaultPassword() {
        return hasDefaultPassword;
    }

    public void setHasDefaultPassword(boolean hasDefaultPassword) {
        this.hasDefaultPassword = hasDefaultPassword;
    }

    public String getChangePasswordToken() {
        return changePasswordToken;
    }

    public void setChangePasswordToken(String changePasswordToken) {
        this.changePasswordToken = changePasswordToken;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
