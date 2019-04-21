package com.javacourse.coursework.models;

import com.javacourse.coursework.Logger;
import com.javacourse.coursework.adapters.DBAdapter;

import java.sql.*;
import java.util.ArrayList;

public class SpecialtyDAO {

    public static ArrayList<Speciality> getAll() {
        Connection connection = DBAdapter.getConnection();
        ArrayList<Speciality> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM `specialities` WHERE 1");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                Speciality speciality = new Speciality(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"));
                result.add(speciality);
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public static Speciality get(int id) {
        Connection connection = DBAdapter.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM `specialities` WHERE `id`=" + id);
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()) {
                return new Speciality(id, resultSet.getString("name"), resultSet.getString("description"));
            } else {
                Logger.write("invalid speciality id");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static Speciality create(String name, String description) {
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `specialities`(`name`, `description`) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return new Speciality(resultSet.getInt(1), name, description);
            } else {
                Logger.write("invalid speciality create");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static Speciality create(int id, String name, String description) {
        Connection connection = DBAdapter.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `specialities`(`id`, `name`, `description`) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return new Speciality(resultSet.getInt(1), name, description);
            } else {
                Logger.write("invalid speciality create");
                return null;
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
        return null;
    }

}