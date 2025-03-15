package com.qubex.learn_now.service;

import com.fasterxml.jackson.core.Base64Variant;
import com.qubex.learn_now.dto.request.UserRegistrationRequestDTO;
import com.qubex.learn_now.exception.custom.AlreadyExistsException;
import com.qubex.learn_now.mapper.UserRegistrationMapper;
import com.qubex.learn_now.model.User;
import com.qubex.learn_now.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationMapper userRegistrationMapper;
    private final PasswordEncoder passwordEncoder;


    public User registerUser(UserRegistrationRequestDTO dto){

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new AlreadyExistsException("Provided email already registered");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = userRegistrationMapper.toEntity(dto);
        user.setRoles(new HashSet<>(Collections.singletonList(dto.getRole())) );

        log.info("User role :{}",user.getRoles().toString());

        return userRepository.save(user);
    }

}



