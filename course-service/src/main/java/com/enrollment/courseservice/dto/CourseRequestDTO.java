package com.enrollment.courseservice.dto;

import jakarta.validation.constraints.*;

public class CourseRequestDTO {

    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Instructor name is required")
    private String instructor;

    @Min(value = 1, message = "Duration must be at least 1 week")
    @Max(value = 52, message = "Duration must not exceed 52 weeks")
    private int duration;

    @Min(value = 1, message = "Max students must be at least 1")
    @Max(value = 500, message = "Max students must not exceed 500")
    private int maxStudents;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    public CourseRequestDTO() {}

    public CourseRequestDTO(String name, String description, String instructor,
                            int duration, int maxStudents, double price) {
        this.name = name;
        this.description = description;
        this.instructor = instructor;
        this.duration = duration;
        this.maxStudents = maxStudents;
        this.price = price;
    }

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
