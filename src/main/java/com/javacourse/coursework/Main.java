package com.javacourse.coursework;

import com.javacourse.coursework.adapters.DBAdapter;
import com.javacourse.coursework.models.Speciality;
import com.javacourse.coursework.models.SpecialtyDAO;
import com.javacourse.coursework.models.Student;
import com.javacourse.coursework.models.StudentDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static ArrayList<Student> localStudents = new ArrayList<>();
    public static ArrayList<Speciality> localSpecialities = new ArrayList<>();

    public static void main(String[] args) {

        DBAdapter.getConnection();

        localStudents = StudentDAO.getAll();
        localSpecialities = SpecialtyDAO.getAll();

        Scanner scanner = new Scanner(System.in);
        for(;;) {
            System.out.println("enter command (type help to get commands list):");
            String[] rawCommand = scanner.nextLine().trim().split(" ");
            if(rawCommand.length > 0) {
                ArrayList<String> arguments = new ArrayList<>(Arrays.asList(rawCommand).subList(1, rawCommand.length));
                CommandHandler.handle(rawCommand[0], arguments);
            }
        }
    }

}