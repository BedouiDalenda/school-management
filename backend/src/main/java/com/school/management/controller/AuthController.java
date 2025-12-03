package com.school.management.controller;

import com.school.management.dto.request.LoginRequest;
import com.school.management.dto.request.RegisterRequest;
import com.school.management.dto.response.LoginResponse;
import com.school.management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    
    private final AuthService authService;
   
   
    @GetMapping("/ping")
    @Operation(summary = "Ping", description = "Test endpoint")
    public String ping() {
    return "OK";
   }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate admin and get JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
@PostMapping("/register")
@Operation(
    summary = "Register new admin", 
    description = "Create a new admin account. Only accessible by authenticated admins."
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Admin created successfully"),
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT required"),
    @ApiResponse(responseCode = "409", description = "Username already exists"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
})
public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
    String currentAdminUsername = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();
    
    authService.register(request, currentAdminUsername);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}

}