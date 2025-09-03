package com.example.jenkins.doctor.dto;

import jakarta.validation.constraints.*;

public record DoctorRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 120) String specialty,
        @Size(max = 160) String hospital,
        @Min(0) @Max(5) Integer rating
) {}
