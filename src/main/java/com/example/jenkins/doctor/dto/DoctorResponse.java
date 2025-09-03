package com.example.jenkins.doctor.dto;


import java.time.Instant;

public record DoctorResponse(
        Long id,
        String name,
        String specialty,
        String hospital,
        Integer rating,
        Instant createdAt,
        Instant updatedAt
) {}

