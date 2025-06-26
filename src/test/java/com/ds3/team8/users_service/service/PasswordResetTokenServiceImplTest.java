package com.ds3.team8.users_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ds3.team8.users_service.exceptions.BadRequestException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Optional;

import com.ds3.team8.users_service.dtos.ForgotPasswordRequest;
import com.ds3.team8.users_service.dtos.PasswordResetRequest;
import com.ds3.team8.users_service.entities.PasswordResetToken;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.repositories.IPasswordResetTokenRepository;
import com.ds3.team8.users_service.repositories.IUserRepository;
import com.ds3.team8.users_service.services.PasswordResetTokenServiceImpl;

class PasswordResetTokenServiceImplTest {

    private IPasswordResetTokenRepository tokenRepo;
    private IUserRepository userRepo;
    private JavaMailSender mailSender;
    private PasswordEncoder passwordEncoder;
    private PasswordResetTokenServiceImpl service;

    @BeforeEach
    void setUp() {
        tokenRepo = mock(IPasswordResetTokenRepository.class);
        userRepo = mock(IUserRepository.class);
        mailSender = mock(JavaMailSender.class);
        passwordEncoder = mock(PasswordEncoder.class);

        service = new PasswordResetTokenServiceImpl(tokenRepo, userRepo, mailSender, passwordEncoder);
        // Setear el valor del resetPasswordUrl manualmente porque no se inyecta en test
        service.resetPasswordUrl = "http://localhost:3000/reset-password";
    }

    @Test
    void sendPasswordResetEmail_shouldSendEmailAndSaveToken() {
        User user = new User();
        user.setEmail("test@example.com");

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        when(userRepo.findByEmailAndIsActiveTrue("test@example.com")).thenReturn(Optional.of(user));

        service.sendPasswordResetEmail(request);

        verify(tokenRepo).save(any(PasswordResetToken.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_shouldThrowIfUserNotFound() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("notfound@example.com");

        when(userRepo.findByEmailAndIsActiveTrue("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.sendPasswordResetEmail(request));
        verify(tokenRepo, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void resetPassword_shouldResetPasswordAndDeleteToken() {
        String tokenStr = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail("reset@example.com");
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiration(LocalDateTime.now().plusMinutes(10));

        PasswordResetRequest request = new PasswordResetRequest();
        request.setToken(tokenStr);
        request.setNewPassword("newpass123");
        request.setConfirmPassword("newpass123");

        when(tokenRepo.findByToken(tokenStr)).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("newpass123")).thenReturn("encodedpass");

        service.resetPassword(request);

        assertEquals("encodedpass", user.getPassword());
        verify(userRepo).save(user);
        verify(tokenRepo).delete(token);
    }

    @Test
    void resetPassword_shouldThrowIfPasswordsDoNotMatch() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setToken("token123");
        request.setNewPassword("pass1");
        request.setConfirmPassword("pass2");

        assertThrows(BadRequestException.class, () -> service.resetPassword(request));
        verify(userRepo, never()).save(any());
    }

    @Test
    void resetPassword_shouldThrowIfTokenNotFound() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setToken("token123");
        request.setNewPassword("pass");
        request.setConfirmPassword("pass");

        when(tokenRepo.findByToken("token123")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.resetPassword(request));
        verify(userRepo, never()).save(any());
    }

    @Test
    void resetPassword_shouldThrowIfTokenExpired() {
        User user = new User();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("expiredToken");
        token.setUser(user);
        token.setExpiration(LocalDateTime.now().minusMinutes(5));

        PasswordResetRequest request = new PasswordResetRequest();
        request.setToken("expiredToken");
        request.setNewPassword("pass123");
        request.setConfirmPassword("pass123");

        when(tokenRepo.findByToken("expiredToken")).thenReturn(Optional.of(token));

        assertThrows(BadRequestException.class, () -> service.resetPassword(request));
        verify(userRepo, never()).save(any());
    }
}
