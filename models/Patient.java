package com.abylay.task1.models;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "patients")
public class Patient {
    public enum Gender {male, female, other};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String surname;
    @NotNull
    @Column(unique = true)
    private String iin;
    private String phone;
    private Gender gender;

    public Patient() {
    }

    public Patient(long id, String firstName, String lastName, String surname, String iin, String phone, Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.iin = iin;
        this.phone = phone;
        this.gender = gender;
    }

    public long getId() {
        return id;
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

    public String getIin() {
        return iin;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setIin(String iin) {
        this.iin = iin;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}