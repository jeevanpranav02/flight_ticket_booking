package com.round.airplaneticketbooking.constants.response;

import lombok.Builder;

@Builder
public class AuthenticationToken {
    private String token;

    public AuthenticationToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
