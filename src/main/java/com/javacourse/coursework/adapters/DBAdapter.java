package com.javacourse.coursework.adapters;

import com.javacourse.coursework.Logger;

import java.sql.*;

public class DBAdapter {

    private static Connection connection = null;

    private static void checkTables() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "students", null);
            if (!resultSet.next()) {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE `javacource`.`specialities` ( `id` INT NOT NULL AUTO_INCREMENT , `name` TEXT NOT NULL , `description` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
                Logger.write("specialities table created");
            }
            resultSet = metaData.getTables(null, null, "students", null);
            if (!resultSet.next()) {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE `javacource`.`students` ( `id` INT NOT NULL AUTO_INCREMENT , `name` TEXT NOT NULL , `speciality` INT NOT NULL, `mark` FLOAT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
                statement.execute("ALTER TABLE `students` ADD CONSTRAINT `specs_link` FOREIGN KEY (`speciality`) REFERENCES `specialities`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;");
                Logger.write("students table created");
            }
        } catch (SQLException e) {
            Logger.write(e.getMessage());
            System.exit(0);
        }
    }

    public static Connection getConnection() {
        if(connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacource?useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
                Logger.write("db connection created");
                checkTables();
                return connection;
            } catch (ClassNotFoundException | SQLException e) {
                Logger.write(e.getMessage());
                System.exit(0);
            }
        } else return connection;

        return null;
    }

    public static void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
                Logger.write("db connection closed");
            } catch (SQLException e) {
                Logger.write(e.getMessage());
                System.exit(0);
            }
        }
    }

    public static void refresh() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE students");
            statement.execute("DROP TABLE specialities");
            checkTables();
            Logger.write("db refreshed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}