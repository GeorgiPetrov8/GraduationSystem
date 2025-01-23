package com.example.Graduation_System.data;

import lombok.Builder;

@Builder(toBuilder = true)
public record LoginRequest (
     String username,
     String password) {
}
