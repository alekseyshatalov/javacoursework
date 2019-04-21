package com.javacourse.coursework.models;

public class Student {

    public int id;
    public String name;
    public Speciality speciality;
    public float mark;

    public Student(int id, String name, Speciality speciality, float mark) {
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