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
    public PatientResponse addPatient(@RequestBody @Valid Patient patient, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.addPatient(patient, isAdmin, userId, username);
    }

    @GetMapping
    public List<PatientResponse> getAllPatients(Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.getAllPatients(isAdmin, userId, username);
    }

    @GetMapping("/{id}")
    public PatientResponse getPatientById(@PathVariable long id, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.getPatientById(id, isAdmin, userId, username);
    }

    @PutMapping
    public PatientResponse updatePatient(@RequestBody Patient patient, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        return patientService.updatePatient(patient, isAdmin, userId, username);
    }

    @DeleteMapping("/{id}")
    public void deletePatientById(@PathVariable long id, Principal principal) {
        boolean isAdmin = isAdmin(principal);
        Long userId = extractUserId(principal);
        String username = principal.getName();
        patientService.deletePatient(id, userId, username);
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
