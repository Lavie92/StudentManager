package com.example.btcn.models;

import androidx.room.PrimaryKey;

import java.util.HashMap;

public class Student {

    @PrimaryKey(autoGenerate = true)
    private String id;

    private String name;

    public Student(String id, String name, String facultyId, double gpa) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
        this.gpa = gpa;
    }

    private String facultyId;

    public Student() {

    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    private double gpa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public Student(String name, String facultyId) {
        this.name = name;
        this.facultyId = facultyId;
    }
    public HashMap<String, Object> convertToHashMap() {
        HashMap<String, Object> studentMap = new HashMap<>();
        studentMap.put("id", this.id);
        studentMap.put("name", this.name);
        studentMap.put("gpa", this.gpa);
        studentMap.put("facultyId", this.facultyId);

        return studentMap;
    }
}