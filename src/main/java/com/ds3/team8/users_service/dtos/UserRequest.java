package com.ds3.team8.users_service.dtos;

import com.ds3.team8.users_service.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    
    @NotBlank(message = "El campo 'firstName' es obligatorio")
    @Size(min = 2, max = 50, message = "El campo 'firstName' debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El campo 'lastName' es obligatorio")
    @Size(min = 2, max = 50, message = "El campo 'lastName' debe tener entre 2 y 50 caracteres")
    private String lastName;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser un correo electrónico válido")
    private String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "El campo 'password' debe tener al menos 8 caracteres, incluyendo al menos un número, una letra y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El campo 'confirmPassword' es obligatorio")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "El campo 'confirmPassword' debe tener al menos 8 caracteres, incluyendo al menos un número, una letra y un carácter especial"
    )
    private String confirmPassword;

    @NotBlank(message = "El campo 'phone' es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "El campo 'phone' debe ser un número de teléfono válido, con un máximo de 15 dígitos y opcionalmente comenzando con '+'"
    )
    private String phone;

    @NotBlank(message = "El campo 'address' es obligatorio")
    @Size(min = 5, max = 100, message = "El campo 'address' debe tener entre 5 y 100 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s,.-]+$",
            message = "El campo 'address' solo puede contener letras, números, espacios, comas, puntos y guiones"
    )
    private String address;

    private Role role;

}
