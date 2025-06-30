package com.abylay.task1.controller;

import com.abylay.task1.DTOs.PatientResponse;
import com.abylay.task1.models.Patient;
import com.abylay.task1.service.AccessLogService;
import com.abylay.task1.service.PatientService;
import com.abylay.task1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final UserService userService;

    PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping
    public Patient addPatient(@RequestBody @Valid Patient patient) {
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
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.getByIin(iin, isAdmin, userId, username);
    }

    @GetMapping("/firstname/{firstname}")
    public List<PatientResponse> getPatientByFirstname(@PathVariable String firstname, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.findAllByFirstName(firstname, isAdmin, userId, username);
    }

    @GetMapping("/lastname/{lastname}")
    public List<PatientResponse> getPatientByLastname(@PathVariable String lastname, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.findAllByLastName(lastname, isAdmin, userId, username);
    }

    private boolean isAdmin(Principal principal) {
        if (principal instanceof JwtAuthenticationToken token) {
            System.out.println("Authorities: " + token.getAuthorities());
            return token.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    private Long extractUserId(Principal principal) {
        if (principal instanceof JwtAuthenticationToken token) {
            Long userId = userService.findByUsername(principal.getName()).getId();
            return userId;
        }
        return null;
    }
}
