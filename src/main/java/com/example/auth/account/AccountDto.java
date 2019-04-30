package com.example.auth.account;

import lombok.Data;

public class AccountDto {

    @Data
    public static class Response {
        private String name;
        private String email;
        private String provider;
        private String subject;
        private String imageUrl;
    }

    @Data
    public static class Principal {
        private String email;
    }
}
