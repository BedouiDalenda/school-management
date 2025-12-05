package com.school.management.service;

import com.school.management.dto.request.LoginRequest;
import com.school.management.dto.request.RegisterRequest;
import com.school.management.dto.response.LoginResponse;
import com.school.management.dto.response.RegisterResponse;
import com.school.management.entity.Admin;
import com.school.management.exception.DuplicateResourceException;
import com.school.management.exception.UnauthorizedException;
import com.school.management.repository.AdminRepository;
import com.school.management.mapper.AdminMapper;
import com.school.management.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public LoginResponse login(LoginRequest request) {
        Admin admin = adminRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        
            // Mot de passe stocké hashé avec passwordEncoder
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        // Génère un JWT
        String token = jwtUtil.generateToken(admin.getUsername());

        return AdminMapper.toLoginResponse(admin, token);
    }
    
    public  RegisterResponse register(RegisterRequest request, String currentAdminUsername) {
    
    if (adminRepository.existsByUsername(request.getUsername())) {
        throw new DuplicateResourceException("Username already exists");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());
    
    Admin admin = AdminMapper.toEntity(request, encodedPassword);
    
    Admin savedAdmin = adminRepository.save(admin);
    
    return AdminMapper.toRegisterResponse(savedAdmin);
}

}