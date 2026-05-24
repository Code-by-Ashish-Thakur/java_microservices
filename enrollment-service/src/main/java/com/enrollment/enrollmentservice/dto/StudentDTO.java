package com.enrollment.enrollmentservice.dto;

// Represents data received from Student Service via RestTemplate

public class StudentDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public StudentDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
