package com.javacourse.coursework.models;

import com.javacourse.coursework.adapters.DBAdapter;

import java.sql.*;
import java.util.ArrayList;

public class SpecialtyDAO {

    public static ArrayList<Speciality> getAll() throws SQLException {
        Connection connection = DBAdapter.getConnection();
        ArrayList<Speciality> result = new ArrayList<>();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM `specialities` WHERE 1");
        ResultSet resultSet = statement.getResultSet();
        while(resultSet.next()) {
            Speciality speciality = new Speciality(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"));
            result.add(speciality);
        }
        return result;
    }

    public static Speciality get(int id) throws SQLException, NullPointerException {
        Connection connection = DBAdapter.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM `specialities` WHERE `id`=" + id);
        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) return new Speciality(id, resultSet.getString("name"), resultSet.getString("description"));
        else throw new NullPointerException("speciality with " + id + " does not exists");
    }

    public static void create(String name, String description) throws SQLException {
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `specialities`(`name`, `description`) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, description);
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) new Speciality(resultSet.getInt(1), name, description);
        else throw new SQLException("new speciality does not created");
    }

    public static void create(int id, String name, String description) throws SQLException {
        Connection connection = DBAdapter.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO `specialities`(`id`, `name`, `description`) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id);
        statement.setString(2, name);
        statement.setString(3, description);
        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) new Speciality(resultSet.getInt(1), name, description);
        else throw new SQLException("new speciality does not created");
    }

}