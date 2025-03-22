package com.qubex.learn_now.model;


import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="instructors")
@Data
@PrimaryKeyJoinColumn(name = "id")
public class Instructor extends User {

    private String bio;
    private String expertise;
    private float averageRating;

}
