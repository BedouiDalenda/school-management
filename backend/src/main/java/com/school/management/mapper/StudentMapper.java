package com.school.management.mapper;

import com.school.management.dto.request.StudentRequest;
import com.school.management.dto.response.StudentResponse;
import com.school.management.entity.Student;

public class StudentMapper {
    
    // Private constructor pour empêcher l'instanciation
    private StudentMapper() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Entity -> Response DTO
    public static StudentResponse toResponse(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getUsername(),
            student.getLevel()
        );
    }
    
    // Request DTO -> Entity (pour CREATE)
    public static Student toEntity(StudentRequest request) {
        return new Student(
            null,  // ID sera généré par la DB
            request.getUsername(),
            request.getLevel()
        );
    }
    
    // Request DTO -> Entity existant (pour UPDATE)
    public static void updateEntity(Student student, StudentRequest request) {
        student.setUsername(request.getUsername());
        student.setLevel(request.getLevel());
    }
}