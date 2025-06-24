package com.ds3.team8.users_service.services;

import com.ds3.team8.users_service.dtos.ForgotPasswordRequest;
import com.ds3.team8.users_service.dtos.PasswordResetRequest;

public interface IPasswordResetTokenService {
    void sendPasswordResetEmail(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(PasswordResetRequest passwordResetRequest);
}
