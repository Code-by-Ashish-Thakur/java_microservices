package com.enrollment.enrollmentservice.dto;

// Represents data received from Course Service via RestTemplate

public class CourseDTO {

    private Long id;
    private String name;
    private String description;
    private String instructor;
    private int duration;
    private int maxStudents;
    private double price;

    public CourseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
