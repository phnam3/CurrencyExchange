package com.example.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class RegisterUserInfo {
    @NotBlank(message = "Email is mandatory")
    @NotEmpty(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private final String email;

    @NotBlank(message = "Role is mandatory")
    @NotEmpty(message = "Role is mandatory")
    private final String roles;

    @NotBlank(message = "Password is mandatory")
    @NotEmpty(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$")
    private final String password;

}
