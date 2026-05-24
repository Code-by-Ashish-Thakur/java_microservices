package com.enrollment.studentservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enrollment.studentservice.dto.StudentRequestDTO;
import com.enrollment.studentservice.entity.Student;
import com.enrollment.studentservice.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        log.info("[STUDENT-CONTROLLER] POST /api/students - Creating student: {}", requestDTO.getEmail());
        Student created = studentService.createStudent(requestDTO);
        log.info("[STUDENT-CONTROLLER] Student created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        log.info("[STUDENT-CONTROLLER] GET /api/students - Fetching all students");
        List<Student> students = studentService.getAllStudents();
        log.info("[STUDENT-CONTROLLER] Returning {} students", students.size());
        return students;
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        log.info("[STUDENT-CONTROLLER] GET /api/students/{} - Fetching student", id);
        return studentService.getStudentById(id);
    }
}
