package com.abylay.task1.repository;

import com.abylay.task1.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional <Patient> findByIin(String iin); // с маленькими буквами
    List<Patient> findAllByFirstName(String firstName);
    List<Patient> findAllByLastName(String firstName);
}

