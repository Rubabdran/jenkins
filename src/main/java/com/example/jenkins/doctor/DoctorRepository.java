package com.example.jenkins.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByNameIgnoreCaseContaining(String q);
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}

