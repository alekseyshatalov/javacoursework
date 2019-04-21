package com.javacourse.coursework;

import com.javacourse.coursework.adapters.DBAdapter;
import com.javacourse.coursework.adapters.ExportAdapter;
import com.javacourse.coursework.adapters.ImportAdapter;
import com.javacourse.coursework.models.Speciality;
import com.javacourse.coursework.models.SpecialtyDAO;
import com.javacourse.coursework.models.Student;
import com.javacourse.coursework.models.StudentDAO;

import java.util.ArrayList;

public class CommandHandler {

    public static void handle(String command, ArrayList<String> arguments) {

        if (command.equals("exit"))  {
            System.exit(0);
            DBAdapter.closeConnection();
        } else

        if (command.equals("help")) printHelp(); else

        if (command.equals("export")) export(arguments); else

        if (command.equals("import")) importCommand(arguments); else

        if (command.equals("refresh")) DBAdapter.refresh(); else

        if (command.equals("show")) show(arguments); else

        if (command.equals("create")) create(arguments); else

        if (command.equals("find_by_speciality")) findBySpeciality(arguments); else

        if (command.equals("average_speciality_mark")) averageSpecialityMark(arguments); else

        System.out.println("undefined command");
    }

    private static void printHelp() {
        System.out.println("exit - stop application work");
        System.out.println("help - show all commands descriptions");
        System.out.println("export [entity] file_path file_name - exports all data or defined entity");
        System.out.println("import file_path file_name - exports all data or special entity");
        System.out.println("refresh - drop and create again all tables");
        System.out.println("show [entity] [id] - show all/defined entity/defined entity line data");
        System.out.println("create entity [name speciality_id mark | name description] - create student/speciality record");
        System.out.println("find_by_speciality speciality_name - find and show all students with defined speciality");
        System.out.println("average_speciality_mark speciality_name - show average mark");
    }

    private static void findBySpeciality(ArrayList<String> arguments) {
        for (Student student : StudentDAO.findBySpeciality(arguments.get(0))) System.out.println(student.toString(true));
    }

    private static void averageSpecialityMark(ArrayList<String> arguments) {
        System.out.println(StudentDAO.averageSpecialityMark(arguments.get(0)));
    }

    private static void create(ArrayList<String> arguments) {
        if (arguments.get(0).equals("students") && arguments.size() == 4) {
            String name = arguments.get(1);
            Speciality speciality = SpecialtyDAO.get(Integer.parseInt(arguments.get(2)));
            float mark = Float.parseFloat(arguments.get(3));
            Main.localStudents.add(StudentDAO.create(name, speciality, mark));
        } else if (arguments.get(0).equals("specialities") && arguments.size() == 3) {
            String name = arguments.get(1);
            String description = arguments.get(2);
            Main.localSpecialities.add(SpecialtyDAO.create(name, description));
        } else System.out.println("wrong command format");
    }

    private static void show(ArrayList<String> arguments) {
        if(arguments.size() > 2) System.out.println("wrong command format");
        else {
            if (arguments.size() == 0) {
                for (Student student : Main.localStudents) System.out.println(student.toString(true));
            }
            if (arguments.size() == 1) {
                if (arguments.get(0).equals("students")) for (Student student : Main.localStudents) System.out.println(student.toString(false));
                if (arguments.get(0).equals("specialities")) for (Speciality speciality : Main.localSpecialities) System.out.println(speciality.toString());
            }
            if (arguments.size() == 2) {
                if (arguments.get(0).equals("students")) System.out.println(StudentDAO.get(Integer.parseInt(arguments.get(1))).toString(false));
                if (arguments.get(0).equals("specialities")) System.out.println(SpecialtyDAO.get(Integer.parseInt(arguments.get(1))).toString());
            }
        }
    }

    private static void importCommand(ArrayList<String> arguments) {
        ImportAdapter.importData(arguments.get(0), arguments.get(1));
        Main.localStudents = StudentDAO.getAll();
        Main.localSpecialities = SpecialtyDAO.getAll();
    }

    private static void export(ArrayList<String> arguments) {
        if(arguments.size() < 2 || arguments.size() > 3) System.out.println("wrong command format");
        else {
            if (arguments.size() == 2) ExportAdapter.export(Main.localStudents, true, arguments.get(0), arguments.get(1));
            else {
                if (arguments.get(0).equals("students")) ExportAdapter.export(Main.localStudents, false, arguments.get(1), arguments.get(2));
                if (arguments.get(0).equals("specialities")) ExportAdapter.export(Main.localSpecialities, arguments.get(1), arguments.get(2));
            }
        }
    }

}