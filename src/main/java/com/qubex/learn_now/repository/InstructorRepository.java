package com.qubex.learn_now.repository;

import com.qubex.learn_now.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    Optional<Instructor> findByEmail(String email);

}
