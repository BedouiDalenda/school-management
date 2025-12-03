package com.school.management.repository;

import com.school.management.entity.Student;
import com.school.management.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    // Filtrer par niveau avec pagination
    Page<Student> findByLevel(Level level, Pageable pageable);
    
    // Rechercher par username ou ID
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "CAST(s.id AS string) LIKE CONCAT('%', :search, '%')")
    Page<Student> searchStudents(@Param("search") String search, Pageable pageable);
}