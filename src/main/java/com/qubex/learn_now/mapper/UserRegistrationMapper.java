package com.qubex.learn_now.mapper;


import com.qubex.learn_now.dto.request.UserRegistrationRequestDTO;
import com.qubex.learn_now.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {

    User toEntity(UserRegistrationRequestDTO userRegistrationRequestDTO);
}
