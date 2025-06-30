package com.abylay.task1.controller;

import com.abylay.task1.DTOs.PatientResponse;
import com.abylay.task1.models.Patient;
import com.abylay.task1.service.PatientService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @GetMapping
    public List<PatientResponse> getAllPatients(Principal principal) {
        boolean isAdmin = isAdmin(principal);
        return patientService.getAllPatients(isAdmin);
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping
    public PatientResponse updatePatient(@RequestBody Patient patient, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        return patientService.updatePatient(patient, isAdmin);
    }

    @DeleteMapping("/{id}")
    public void deletePatientById(@PathVariable long id) {
        patientService.deletePatient(id);
    }

    @GetMapping("/iin/{iin}")
    public PatientResponse getPatientByIin(@PathVariable String iin, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        return patientService.getByIin(iin, isAdmin);
    }

    @GetMapping("/firstname/{firstname}")
    public List<PatientResponse> getPatientByFirstname(@PathVariable String firstname, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        return patientService.findAllByFirstName(firstname, isAdmin);
    }

    @GetMapping("/lastname/{lastname}")
    public List<PatientResponse> getPatientByLastname(@PathVariable String lastname, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        return patientService.findAllByLastName(lastname, isAdmin);
    }

    private boolean isAdmin(Principal principal) {
        if (principal instanceof JwtAuthenticationToken token) {
            System.out.println("Authorities: " + token.getAuthorities());
            return token.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }
}
