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

    PatientService(PatientRepository patientRepository, EncryptionService encryptionService) {
        this.patientRepository = patientRepository;
        this.encryptionService = encryptionService;
    }

    public Patient addPatient(Patient patient) {
        Patient patientToSave = new Patient();
        patientToSave.setFirstName(encryptionService.encrypt(patient.getFirstName()));
        patientToSave.setLastName(encryptionService.encrypt(patient.getLastName()));
        patientToSave.setSurname(encryptionService.encrypt(patient.getSurname()));
        patientToSave.setGender(patient.getGender());
        patientToSave.setIin(patient.getIin());
        patientToSave.setPhone(patient.getPhone());

        return patientRepository.save(patientToSave);
    }

    public Patient getPatientById(long id) {
        return patientRepository.getReferenceById(id);
    }

    public List<PatientResponse> getAllPatients(boolean isAdmin) {
        List<Patient> patients= patientRepository.findAll();
        List<PatientResponse> patientResponses = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
        }
        return patientResponses;
    }

    public PatientResponse updatePatient(Patient patient, boolean isAdmin) {
        Patient existingPatient = getPatientById(patient.getId());

        if (existingPatient == null) {
            throw new RuntimeException("Patient not found");
        }

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setSurname(patient.getSurname());
        existingPatient.setPhone(patient.getPhone());
        patientRepository.save(existingPatient);
        return mapToResponse(existingPatient, isAdmin);
    }

    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }

    public PatientResponse getByIin(String iin, boolean isAdmin) {
        Patient patient = patientRepository.findByIin(iin).orElse(null);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        return mapToResponse(patient, isAdmin);
    }

    public List<PatientResponse> findAllByFirstName(String firstName, boolean isAdmin) {
        List<Patient> patients = patientRepository.findAllByFirstName(encryptionService.encrypt(firstName));
        List<PatientResponse> patientResponses = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
        }
        return patientResponses;
    }

    public List<PatientResponse> findAllByLastName(String lastName, boolean isAdmin) {
        List<Patient> patients = patientRepository.findAllByLastName(encryptionService.encrypt(lastName));
        List<PatientResponse> patientResponses = new ArrayList<>();
        for (Patient patient : patients) {
            patientResponses.add(mapToResponse(patient, isAdmin));
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
