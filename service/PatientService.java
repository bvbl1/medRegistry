package com.abylay.task1.service;

import com.abylay.task1.DTOs.PatientResponse;
import com.abylay.task1.models.Patient;
import com.abylay.task1.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final EncryptionService encryptionService;
    private final AccessLogService accessLogService;

    PatientService(PatientRepository patientRepository, EncryptionService encryptionService, AccessLogService accessLogService) {
        this.patientRepository = patientRepository;
        this.encryptionService = encryptionService;
        this.accessLogService = accessLogService;
    }

    public PatientResponse addPatient(Patient patient, boolean isAdmin,long userId, String username) {
        Patient patientToSave = new Patient();
        patientToSave.setFirstName(encryptionService.encrypt(patient.getFirstName()));
        patientToSave.setLastName(encryptionService.encrypt(patient.getLastName()));
        patientToSave.setSurname(encryptionService.encrypt(patient.getSurname()));
        patientToSave.setGender(patient.getGender());
        patientToSave.setIin(patient.getIin());
        patientToSave.setPhone(patient.getPhone());

        patientRepository.save(patientToSave);

        accessLogService.logAccess(userId, username, List.of(patientToSave.getIin()), "POST", "/patients");

        return mapToResponse(patientToSave, isAdmin);
    }

    public PatientResponse getPatientById(long id, boolean isAdmin, long userId, String username) {
        Patient existingPatient = patientRepository.getReferenceById(id);
        accessLogService.logAccess(userId, username, List.of(existingPatient.getIin()), "GET", "/patients/" + id);
        return mapToResponse(existingPatient, isAdmin);
    }

    public List<PatientResponse> getAllPatients(boolean isAdmin, long userId, String username) {
        List<Patient> patients= patientRepository.findAll();
        List<PatientResponse> patientResponses = new ArrayList<>();
        List<String> accessIins = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
            accessIins.add(patient.getIin());
        }
        if (!accessIins.isEmpty()) {
            accessLogService.logAccess(userId, username, accessIins, "GET", "/patients");
        }
        return patientResponses;
    }

    public PatientResponse updatePatient(Patient patient, boolean isAdmin, long userId, String username) {
        Patient existingPatient = patientRepository.getReferenceById(patient.getId());

        if (existingPatient == null) {
            throw new RuntimeException("Patient not found");
        }

        existingPatient.setFirstName(encryptionService.encrypt(patient.getFirstName()));
        existingPatient.setLastName(encryptionService.encrypt(patient.getLastName()));
        existingPatient.setSurname(encryptionService.encrypt(patient.getSurname()));
        existingPatient.setPhone(patient.getPhone());
        patientRepository.save(existingPatient);

        accessLogService.logAccess(userId, username, List.of(existingPatient.getIin()), "PUT", "/patients/" + patient.getId());

        return mapToResponse(existingPatient, isAdmin);
    }

    public void deletePatient(long id, long userId, String username) {
        Patient existingPatient = patientRepository.getReferenceById(id);
        patientRepository.deleteById(id);
        accessLogService.logAccess(userId, username, List.of(existingPatient.getIin()), "DELETE", "/patients/" + id);
    }

    public PatientResponse getByIin(String iin, boolean isAdmin, long userId, String username) {
        Patient patient = patientRepository.findByIin(iin).orElse(null);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }
        accessLogService.logAccess(userId, username, List.of(iin), "GET", "/patient/iin/" + iin);

        return mapToResponse(patient, isAdmin);
    }

    public List<PatientResponse> findAllByFirstName(String firstName, boolean isAdmin, long userId, String username) {
        List<Patient> patients = patientRepository.findAllByFirstName(encryptionService.encrypt(firstName));
        List<PatientResponse> patientResponses = new ArrayList<>();
        List<String> accessIins = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
            accessIins.add(patient.getIin());
        }

        if (!accessIins.isEmpty()) {
            accessLogService.logAccess(userId, username, accessIins, "GET", "/patient/firstname/" + firstName);
        }
        return patientResponses;
    }

    public List<PatientResponse> findAllByLastName(String lastName, boolean isAdmin, long userId, String username) {
        List<Patient> patients = patientRepository.findAllByLastName(encryptionService.encrypt(lastName));
        List<PatientResponse> patientResponses = new ArrayList<>();
        List<String> accessIins = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
            accessIins.add(patient.getIin());
        }

        if (!accessIins.isEmpty()) {
            accessLogService.logAccess(userId, username, accessIins, "GET", "/patient/lastname/" + lastName);
        }
        return patientResponses;
    }

    //mappers
    private PatientResponse mapToResponse(Patient p, boolean isAdmin) {
        return new PatientResponse(
                p.getIin(),
                isAdmin ? encryptionService.decrypt(p.getFirstName()) : "hidden",
                isAdmin ? encryptionService.decrypt(p.getLastName()) : "hidden",
                isAdmin ? encryptionService.decrypt(p.getSurname()) : "hidden",
                p.getGender(),
                p.getIin(),
                p.getPhone()
        );
    }
}
