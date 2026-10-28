package com.example.midexam;

public class Student {
    private long id;
    private String studentId;
    private String studentName;
    private boolean isEnrolled;

    public Student() {
    }

    public Student(long id, String studentId, String studentName, boolean isEnrolled) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.isEnrolled = isEnrolled;
    }

    public Student(String studentId, String studentName, boolean isEnrolled) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.isEnrolled = isEnrolled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}
