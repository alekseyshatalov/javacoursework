package com.javacourse.coursework.models;

public class Speciality {

    public int id;
    public String name;
    public String description;

    public Speciality(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.id, this.name, this.description);
    }

}