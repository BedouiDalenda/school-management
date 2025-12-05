package com.school.management.mapper;

import com.school.management.dto.request.RegisterRequest;
import com.school.management.dto.response.LoginResponse;
import com.school.management.dto.response.RegisterResponse;
import com.school.management.entity.Admin;

import java.time.LocalDateTime;

public class AdminMapper {
    
    private AdminMapper() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // RegisterRequest -> Entity 
    public static Admin toEntity(RegisterRequest request, String encodedPassword) {
        return new Admin(
            null,  // ID sera généré par la DB
            request.getUsername(),
            encodedPassword  // Mot de passe déjà encodé
        );
    }
    
    // Admin + Token -> LoginResponse
    public static LoginResponse toLoginResponse(Admin admin, String token) {
        return new LoginResponse(
            token,
            admin.getUsername()
        );
    }
    
    // Admin -> RegisterResponse
    public static RegisterResponse toRegisterResponse(Admin admin) {
        return new RegisterResponse(
            "Admin registered successfully",
            admin.getUsername(),
            LocalDateTime.now()
        );
    }
}