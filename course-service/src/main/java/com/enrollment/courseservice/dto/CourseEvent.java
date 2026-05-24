package com.enrollment.courseservice.dto;

public class CourseEvent {

    private String eventType;    // COURSE_CREATED, COURSE_UPDATED, COURSE_DELETED
    private Long courseId;
    private String name;
    private String instructor;
    private double price;
    private long timestamp;

    public CourseEvent() {}

    public CourseEvent(String eventType, Long courseId, String name,
                       String instructor, double price) {
        this.eventType = eventType;
        this.courseId = courseId;
        this.name = name;
        this.instructor = instructor;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "CourseEvent{eventType='" + eventType + "', courseId=" + courseId +
                ", name='" + name + "', price=" + price + "}";
    }
}
