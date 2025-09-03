package com.example.jenkins.doctor;

import com.example.jenkins.doctor.dto.DoctorRequest;
import com.example.jenkins.doctor.dto.DoctorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @GetMapping
    public List<DoctorResponse> list(@RequestParam(required = false) String q,
                                     @RequestParam(required = false) String specialty) {
        return service.listAll(q, specialty).stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DoctorResponse get(@PathVariable Long id) {
        return toDto(service.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse create(@Valid @RequestBody DoctorRequest req) {
        var d = new Doctor();
        d.setName(req.name());
        d.setSpecialty(req.specialty());
        d.setHospital(req.hospital());
        d.setRating(req.rating());
        return toDto(service.create(d));
    }

    @PatchMapping("/{id}")
    public DoctorResponse update(@PathVariable Long id, @RequestBody DoctorRequest req) {
        var patch = new Doctor();
        patch.setName(req.name());
        patch.setSpecialty(req.specialty());
        patch.setHospital(req.hospital());
        patch.setRating(req.rating());
        return toDto(service.update(id, patch));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private DoctorResponse toDto(Doctor d) {
        return new DoctorResponse(
                d.getId(), d.getName(), d.getSpecialty(), d.getHospital(),
                d.getRating(), d.getCreatedAt(), d.getUpdatedAt()
        );
    }
}
