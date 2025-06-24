package com.ds3.team8.users_service.services;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ds3.team8.users_service.dtos.ForgotPasswordRequest;
import com.ds3.team8.users_service.dtos.PasswordResetRequest;
import com.ds3.team8.users_service.entities.PasswordResetToken;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import com.ds3.team8.users_service.repositories.IPasswordResetTokenRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;

@Service
public class PasswordResetTokenServiceImpl implements IPasswordResetTokenService {

    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final IUserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${frontend.reset-password.url}")
    private String resetPasswordUrl;

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);


    public PasswordResetTokenServiceImpl(IPasswordResetTokenRepository passwordResetTokenRepository, IUserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendPasswordResetEmail(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        logger.info("Solicitud de restablecimiento de contraseña recibida para el correo: {}", email);

        // Verificar si el usuario existe y está activo
        Optional<User> existingUser = userRepository.findByEmailAndIsActiveTrue(email);
        if (existingUser.isEmpty()) {
            logger.warn("Intento de restablecimiento de contraseña para un usuario no existente o inactivo: {}", email);
            throw new BadRequestException("El usuario con el correo proporcionado no existe o no está activo.");
        }
        
        // Generar token de restablecimiento de contraseña
        User user = existingUser.get();
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(resetToken);
        logger.info("Token de restablecimiento de contraseña generado para el usuario: {}", email);

        // Enviar email
        String link = resetPasswordUrl + "?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Recuperación de contraseña");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " + link);
        mailSender.send(message);
        logger.info("Correo de restablecimiento de contraseña enviado a: {}", email);
    }


    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        String newPassword = passwordResetRequest.getNewPassword();
        String confirmPassword = passwordResetRequest.getConfirmPassword();

        // Validar que las contraseñas coincidan
        if (!newPassword.equals(confirmPassword)) {
            logger.warn("Las contraseñas no coinciden para el token: {}", token);
            throw new BadRequestException("Las contraseñas no coinciden.");
        }

        // Buscar el token en la base de datos
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            logger.warn("Intento de restablecimiento de contraseña con un token inválido: {}", token);
            throw new BadRequestException("Token de restablecimiento de contraseña inválido.");
        }

        PasswordResetToken resetToken = optionalToken.get();

        // Verificar si el token ha expirado
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("Intento de restablecimiento de contraseña con un token expirado: {}", token);
            throw new BadRequestException("El token de restablecimiento de contraseña ha expirado.");
        }

        // Actualizar la contraseña del usuario
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Contraseña restablecida para el usuario: {}", user.getEmail());

        // Eliminar el token después de usarlo
        passwordResetTokenRepository.delete(resetToken);
        logger.info("Token de restablecimiento de contraseña eliminado para el usuario: {}", user.getEmail());
    }
    
}
