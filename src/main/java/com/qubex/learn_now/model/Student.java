package com.qubex.learn_now.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "students")
@Data
@PrimaryKeyJoinColumn(name = "id")
public class Student extends User {


    private String bio;
    private String preferences;

}
