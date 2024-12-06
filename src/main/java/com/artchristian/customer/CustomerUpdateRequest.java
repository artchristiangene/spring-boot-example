package com.artchristian.customer;

public record CustomerUpdateRequest(
        Integer id,
        String name,
        String email,
        Integer age) {
}