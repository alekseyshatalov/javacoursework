package com.javacourse.coursework;

import com.javacourse.coursework.adapters.DBAdapter;
import com.javacourse.coursework.adapters.ExportAdapter;
import com.javacourse.coursework.adapters.ImportAdapter;
import com.javacourse.coursework.models.Speciality;
import com.javacourse.coursework.models.SpecialtyDAO;
import com.javacourse.coursework.models.Student;
import com.javacourse.coursework.models.StudentDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

class CommandHandler {

    private static final Logger logger = LogManager.getLogger(CommandHandler.class);

    static void handle(String command, ArrayList<String> arguments) {
        switch (command) {
            case "exit":
                try { DBAdapter.closeConnection(); }
                catch (SQLException e) {logger.error("close error: " + e.getMessage());}
                System.exit(0);
            case "help":
                printHelp();
                break;
            case "export":
                export(arguments);
                break;
            case "import":
                importCommand(arguments);
                break;
            case "refresh":
                try { DBAdapter.refresh(); }
                catch (SQLException e) { logger.error("refresh error: " + e.getMessage()); }
                break;
            case "show":
                show(arguments);
                break;
            case "create":
                create(arguments);
                break;
            case "find_by_speciality":
                findBySpeciality(arguments);
                break;
            case "average_speciality_mark":
                averageSpecialityMark(arguments);
                break;
            default:
                logger.error("undefined command");
                break;
        }
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
        try { for (Student student : StudentDAO.findBySpeciality(arguments.get(0))) System.out.println(student.toString(true)); }
        catch (SQLException e) { logger.error("can't find by speciality: " + e.getMessage()); }
    }

    private static void averageSpecialityMark(ArrayList<String> arguments) {
        try { System.out.println(StudentDAO.averageSpecialityMark(arguments.get(0))); }
        catch (SQLException e) { logger.error(e.getMessage()); }
    }

    private static void create(ArrayList<String> arguments) {
        if (arguments.get(0).equals("students") && arguments.size() == 4) {
            try {
                Speciality speciality = SpecialtyDAO.get(Integer.parseInt(arguments.get(2)));
                String name = arguments.get(1);
                float mark = Float.parseFloat(arguments.get(3));
                StudentDAO.create(name, speciality, mark);
            }
            catch (SQLException e) { logger.error(e.getMessage()); }
        } else if (arguments.get(0).equals("specialities") && arguments.size() == 3) {
            try {
                String name = arguments.get(1);
                String description = arguments.get(2);
                SpecialtyDAO.create(name, description);
            }
            catch (SQLException e) { logger.error(e.getMessage()); }
        } else logger.error("wrong command format");
    }

    private static void show(ArrayList<String> arguments) {
        if(arguments.size() > 2) logger.error("wrong command format");
        else {
            if (arguments.size() == 0) {
                try {
                    ArrayList<Student> students = StudentDAO.getAll();
                    for (Student student : students) System.out.println(student.toString(true));
                } catch (SQLException e) { logger.error(e.getMessage()); }
            }
            if (arguments.size() == 1) {
                if (arguments.get(0).equals("students")) {
                    try {
                        ArrayList<Student> students = StudentDAO.getAll();
                        for (Student student : students) System.out.println(student.toString(false));
                    } catch (SQLException e) { logger.error(e.getMessage()); }
                }
                if (arguments.get(0).equals("specialities")) {
                    try {
                        ArrayList<Speciality> specialities = SpecialtyDAO.getAll();
                        for (Speciality speciality : specialities) System.out.println(speciality.toString());
                    } catch (SQLException e) { logger.error(e.getMessage()); }
                }
            }
            if (arguments.size() == 2) {
                if (arguments.get(0).equals("students")) {
                    try { System.out.println(StudentDAO.get(1).toString(false)); }
                    catch (SQLException e) { logger.error(e.getMessage()); }
                }
                if (arguments.get(0).equals("specialities")) {
                    try { System.out.println(SpecialtyDAO.get(1).toString()); }
                    catch (SQLException e) { logger.error(e.getMessage()); }
                }
            }
        }
    }

    private static void importCommand(ArrayList<String> arguments) {
        try { ImportAdapter.importData(arguments.get(0), arguments.get(1)); }
        catch (IOException | SAXException | ParserConfigurationException | SQLException e) {logger.error("import error: " + e.getMessage());}
    }

    private static void export(ArrayList<String> arguments) {
        if(arguments.size() < 2 || arguments.size() > 3) System.out.println("wrong command format");
        else {
            if (arguments.size() == 2) {
                try {
                    ArrayList<Student> students = StudentDAO.getAll();
                    ExportAdapter.export(students, true, arguments.get(0), arguments.get(1));
                } catch (SQLException | IOException e) { logger.error("export error: " + e.getMessage()); }

            }
            else {
                if (arguments.get(0).equals("students")) {
                    try {
                        ArrayList<Student> students = StudentDAO.getAll();
                        ExportAdapter.export(students, false, arguments.get(1), arguments.get(2));
                    } catch (SQLException | IOException e) { logger.error("export error: " + e.getMessage()); }
                }
                if (arguments.get(0).equals("specialities")) {
                    try {
                        ArrayList<Speciality> specialities = SpecialtyDAO.getAll();
                        ExportAdapter.export(specialities, arguments.get(1), arguments.get(2));
                    } catch (SQLException | IOException e) { logger.error("export error: " + e.getMessage()); }
                }
            }
        }
    }

}