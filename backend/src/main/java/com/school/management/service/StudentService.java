package com.school.management.service;

import com.school.management.dto.request.StudentRequest;
import com.school.management.dto.response.StudentResponse;
import com.school.management.entity.Student;
import com.school.management.enums.Level;
import com.school.management.exception.DuplicateResourceException;
import com.school.management.exception.ResourceNotFoundException;
import com.school.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    // GET all students with pagination
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
            .map(this::toResponse);
    }
    
    // GET student by ID
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return toResponse(student);
    }
    
    // CREATE student
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        
        Student student = new Student(null, request.getUsername(), request.getLevel());
        Student savedStudent = studentRepository.save(student);
        return toResponse(savedStudent);
    }
    
    // UPDATE student
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        // Vérifier si le nouveau username existe déjà (sauf pour l'étudiant actuel)
        if (!student.getUsername().equals(request.getUsername()) 
            && studentRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        
        student.setUsername(request.getUsername());
        student.setLevel(request.getLevel());
        
        Student updatedStudent = studentRepository.save(student);
        return toResponse(updatedStudent);
    }
    
    // DELETE student
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    // SEARCH students by username or ID
    public Page<StudentResponse> searchStudents(String search, Pageable pageable) {
        return studentRepository.searchStudents(search, pageable)
            .map(this::toResponse);
    }
    
    // FILTER students by level
    public Page<StudentResponse> filterByLevel(Level level, Pageable pageable) {
        return studentRepository.findByLevel(level, pageable)
            .map(this::toResponse);
    }
    
    // Mapper Entity -> Response DTO
    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getUsername(),
            student.getLevel()
        );
    }
}