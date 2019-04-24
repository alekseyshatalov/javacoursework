package com.javacourse.coursework.adapters;

import java.sql.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DBAdapter {

    private static Connection connection = null;
    private static final Logger logger = LogManager.getLogger(DBAdapter.class);

    private static void checkTables() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "students", null);
        if (!resultSet.next()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE `javacource`.`specialities` ( `id` INT NOT NULL AUTO_INCREMENT , `name` TEXT NOT NULL , `description` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            logger.info("specialities table created");
        }
        resultSet = metaData.getTables(null, null, "students", null);
        if (!resultSet.next()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE `javacource`.`students` ( `id` INT NOT NULL AUTO_INCREMENT , `name` TEXT NOT NULL , `speciality` INT NOT NULL, `mark` FLOAT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            statement.execute("ALTER TABLE `students` ADD CONSTRAINT `specs_link` FOREIGN KEY (`speciality`) REFERENCES `specialities`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;");
            logger.info("students table created");
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacource?useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
                logger.info("connection created");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.fatal("connection is not created");
            System.exit(-1); // Так как подключение к бд основной момент работы приложения, то программу без него придется закрыть
        }
        checkTables();
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if(connection != null) {
            connection.close();
            connection = null;
        }
    }

    public static void refresh() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute("DROP TABLE students");
        statement.execute("DROP TABLE specialities");
        statement.close();
        checkTables();
    }

}