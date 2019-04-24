package com.javacourse.coursework.models;

import com.javacourse.coursework.adapters.DBAdapter;

import java.sql.*;
import java.util.ArrayList;

public class StudentDAO {

    public static ArrayList<Student> getAll() throws SQLException {
        ArrayList<Student> result = new ArrayList<>();
        Connection connection = DBAdapter.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM `students` WHERE 1");
        ResultSet resultSet = statement.getResultSet();
        while(resultSet.next()) {
            Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
            Student student = new Student(resultSet.getInt("id"), resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
            result.add(student);
        }
        return result;
    }

    public static Student get(int id) throws SQLException, NullPointerException {
        Connection connection = DBAdapter.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM `students` WHERE `id`=" + id);
        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) {
            Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
            return new Student(id, resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
        } else throw new NullPointerException("student with " + id + " does not exists");
    }

    public static void create(String name, Speciality speciality, float mark) throws SQLException {
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `students`(`name`, `speciality`, `mark`) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setInt(2, speciality.id);
        statement.setFloat(3, mark);
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) new Student(resultSet.getInt(1), name, speciality, mark);
        else throw new SQLException("new student does not created");
    }

    public static void create(int id, String name, Speciality speciality, float mark) throws SQLException {
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `students`(`id`, `name`, `speciality`, `mark`) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id);
        statement.setString(2, name);
        statement.setInt(3, speciality.id);
        statement.setFloat(4, mark);
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) new Student(resultSet.getInt(1), name, speciality, mark);
        else throw new SQLException("new student does not created");
    }

    public static ArrayList<Student> findBySpeciality(String specialityName) throws SQLException {
        ArrayList<Student> result = new ArrayList<>();
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `students` WHERE `speciality`=(SELECT `id` FROM `specialities` WHERE `name`=?)");
        statement.setString(1, specialityName);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        while(resultSet.next()) {
            Speciality speciality = SpecialtyDAO.get(resultSet.getInt("speciality"));
            Student student = new Student(resultSet.getInt("id"), resultSet.getString("name"), speciality, resultSet.getFloat("mark"));
            result.add(student);
        }
        return result;
    }

    public static float averageSpecialityMark(String specialityName) throws SQLException {
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT AVG(`mark`) as `avg` FROM `students` WHERE `speciality`=(SELECT `id` FROM `specialities` WHERE `name`=?)");
        statement.setString(1, specialityName);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) return  resultSet.getFloat("avg");
        else throw new SQLException("avg mark does not selected");
    }

}