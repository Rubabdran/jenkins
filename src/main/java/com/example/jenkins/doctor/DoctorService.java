package com.example.jenkins.doctor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository repo;

    public DoctorService(DoctorRepository repo) {
        this.repo = repo;
    }

    public List<Doctor> listAll(String q, String specialty) {
        if (q != null && !q.isBlank()) return repo.findByNameIgnoreCaseContaining(q.trim());
        if (specialty != null && !specialty.isBlank()) return repo.findBySpecialtyIgnoreCase(specialty.trim());
        return repo.findAll();
    }

    public Doctor get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
    }

    public Doctor create(Doctor d) {
        d.setId(null);
        return repo.save(d);
    }

    public Doctor update(Long id, Doctor patch) {
        var d = get(id);
        if (patch.getName() != null) d.setName(patch.getName());
        if (patch.getSpecialty() != null) d.setSpecialty(patch.getSpecialty());
        if (patch.getHospital() != null) d.setHospital(patch.getHospital());
        if (patch.getRating() != null) d.setRating(patch.getRating());
        return repo.save(d);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

