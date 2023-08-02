package com.example.btcn.models;


import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Faculty {
    @PrimaryKey(autoGenerate = true)
    private String id;

    private String name;

    @Ignore
    List<Student> students;

    public Faculty() {

    }

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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Faculty(String name) {
        this.name = name;
    }

    public HashMap<String, Object> convertToHashMap() {
        HashMap<String, Object> facultyMap = new HashMap<>();
        facultyMap.put("id", this.id);
        facultyMap.put("name", this.name);
        return facultyMap;
    }

    public int getStudentCount() {
        if(students != null) {
            return students.size();
        }
        return 0;
    }
}