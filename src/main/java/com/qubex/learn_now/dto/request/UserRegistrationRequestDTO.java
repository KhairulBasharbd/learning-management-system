package com.qubex.learn_now.dto.request;

import com.qubex.learn_now.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDTO {

    @NotEmpty(message = "Name can not be empty")
    private String fullName;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Invalid Email format")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long" )
//    @Pattern(
//            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
//            message = "Password must contain at least one digit, lowercase, uppercase and special character"
//    )
    private String password;

    @NotNull(message = "Role can not be empty")
    private Role role;


}
