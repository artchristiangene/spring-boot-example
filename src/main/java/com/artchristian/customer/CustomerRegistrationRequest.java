package com.artchristian.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
