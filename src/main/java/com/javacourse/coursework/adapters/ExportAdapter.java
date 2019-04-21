package com.javacourse.coursework.adapters;

import com.javacourse.coursework.Logger;
import com.javacourse.coursework.models.Speciality;
import com.javacourse.coursework.models.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExportAdapter {

    public static void export(ArrayList<Student> students, boolean withSpeciality, String filePath, String fileName) {
        List<String> strings = new ArrayList<>();
        for (Student student : students) strings.add(student.toString(withSpeciality));
        export(strings, filePath, fileName);
    }

    public static void export(ArrayList<Speciality> specialities, String filePath, String fileName) {
        List<String> strings = new ArrayList<>();
        for (Speciality speciality : specialities) strings.add(speciality.toString());
        export(strings, filePath, fileName);
    }

    private static void export(List<String> strings, String filePath, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "\\" + fileName);
            for (String string : strings) {
                fileOutputStream.write((string+"\n").getBytes());
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            Logger.write("export completed");
        } catch (IOException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
    }

}