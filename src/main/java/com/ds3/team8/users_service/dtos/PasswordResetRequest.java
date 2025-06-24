package com.ds3.team8.users_service.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    
    @NotBlank(message = "El campo 'token' es obligatorio")
      private String token;

      @NotBlank(message = "El campo 'newPassword' es obligatorio")
      @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "El campo 'password' debe tener al menos 8 caracteres, incluyendo al menos un número, una letra y un carácter especial"
      )
      private String newPassword;

      @NotBlank(message = "El campo 'confirmPassword' es obligatorio")
      @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "El campo 'confirmPassword' debe tener al menos 8 caracteres, incluyendo al menos un número, una letra y un carácter especial"
      )
      private String confirmPassword;

}
