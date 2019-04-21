package com.javacourse.coursework.models;

import com.javacourse.coursework.Logger;
import com.javacourse.coursework.adapters.DBAdapter;

import java.sql.*;
import java.util.ArrayList;

public class StudentDAO {

    public static ArrayList<Student> getAll() {
        Connection connection = DBAdapter.getConnection();
        ArrayList<Student> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM `students` WHERE 1");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
                Student student = new Student(resultSet.getInt("id"), resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
                result.add(student);
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public static Student get(int id) {
        Connection connection = DBAdapter.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM `students` WHERE `id`=" + id);
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()) {
                Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
                return new Student(id, resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
            } else {
                Logger.write("invalid student id");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static Student create(String name, Speciality speciality, float mark) {
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `students`(`name`, `speciality`, `mark`) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, speciality.id);
            statement.setFloat(3, mark);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return new Student(resultSet.getInt(1), name, speciality, mark);
            } else {
                Logger.write("invalid student create");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static Student create(int id, String name, Speciality speciality, float mark) {
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `students`(`id`, `name`, `speciality`, `mark`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, speciality.id);
            statement.setFloat(4, mark);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return new Student(resultSet.getInt(1), name, speciality, mark);
            } else {
                Logger.write("invalid student create");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static ArrayList<Student> findBySpeciality(String specialityName) {
        ArrayList<Student> result = new ArrayList<>();
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `students` WHERE `speciality`=(SELECT `id` FROM `specialities` WHERE `name`=?)");
            statement.setString(1, specialityName);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
                Student student = new Student(resultSet.getInt("id"), resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
                result.add(student);
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
        }
        return result;
    }

    public static float averageSpecialityMark(String specialityName) {
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT AVG(`mark`) as `avg` FROM `students` WHERE `speciality`=(SELECT `id` FROM `specialities` WHERE `name`=?)");
            statement.setString(1, specialityName);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getFloat("avg");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            return 0;
        }
    }

}