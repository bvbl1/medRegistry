package com.abylay.task1.DTOs;

import com.abylay.task1.models.Patient;

public class PatientResponse {
    private String iin;
    private String firstName;
    private String lastName;
    private String surname;
    private String phone;
    private Patient.Gender gender;

    public PatientResponse(String iin, String s, String s1, String s2, Patient.Gender gender, String iin1, String phone) {
        this.iin = iin;
        this.firstName = s;
        this.lastName = s1;
        this.surname = s2;
        this.phone = phone;
        this.gender = gender;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Patient.Gender gender) {
        this.gender = gender;
    }

    public String getIin() {
        return iin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public Patient.Gender getGender() {
        return gender;
    }
}
