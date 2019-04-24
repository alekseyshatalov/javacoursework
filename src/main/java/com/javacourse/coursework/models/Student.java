package com.javacourse.coursework.models;

public class Student {

    private int id;
    private String name;
    private Speciality speciality;
    private float mark;

    Student(int id, String name, Speciality speciality, float mark) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.mark = mark;
    }

    public String toString(boolean withSpeciality) {
        if(withSpeciality) {
            return String.format("%s %s %s %s %s", this.id, this.name, this.mark, this.speciality.name, speciality.description);
        } else {
            return String.format("%s %s %s %s", this.id, this.name, this.speciality.id, this.mark);
        }
    }

}