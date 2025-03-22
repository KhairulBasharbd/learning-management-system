package com.qubex.learn_now.service;

import com.qubex.learn_now.dto.request.UserRegistrationRequestDTO;
import com.qubex.learn_now.exception.custom.AlreadyExistsException;
import com.qubex.learn_now.mapper.UserRegistrationMapper;
import com.qubex.learn_now.model.Instructor;
import com.qubex.learn_now.model.Student;
import com.qubex.learn_now.model.User;
import com.qubex.learn_now.repository.InstructorRepository;
import com.qubex.learn_now.repository.StudentRepository;
import com.qubex.learn_now.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerUser(UserRegistrationRequestDTO dto){

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new AlreadyExistsException("Provided email already registered");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        log.info("User role :{}",dto.getRole().toString());

        if("role_student".equalsIgnoreCase(dto.getRole().toString())){

            Student student = new Student();

            student.setFullName( dto.getFullName() );
            student.setEmail( dto.getEmail() );
            student.setPassword( dto.getPassword() );
            student.setRoles(new HashSet<>(Collections.singletonList(dto.getRole())) );

            return studentRepository.save(student);
        }
        else if("role_instructor".equalsIgnoreCase(dto.getRole().toString())){

            Instructor instructor = new Instructor();

            instructor.setFullName( dto.getFullName() );
            instructor.setEmail( dto.getEmail() );
            instructor.setPassword( dto.getPassword() );
            instructor.setRoles(new HashSet<>(Collections.singletonList(dto.getRole())) );

            return instructorRepository.save(instructor);
        }
        else  {
            throw new IllegalArgumentException("Invalid role");
        }
    }



}



