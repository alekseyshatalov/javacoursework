package com.javacourse.coursework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class ProductsAdapter {

    private Connection connection;

    ProductsAdapter() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javacource?useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
    }

    void add(String name, String price, String amount) {
        try {
            Statement statment = connection.createStatement();
            statment.executeUpdate(String.format("INSERT INTO products(name, price, amount) VALUES ('%s',%s,%s)", name, price, amount));
        } catch (SQLException ignored) { }
    }

    List<String> all() {
        List<String> result = new ArrayList<>();

        try {
            Statement statment = connection.createStatement();
            ResultSet resultSet = statment.executeQuery("SELECT * FROM products");
            result = parseResult(resultSet);
        } catch (SQLException ignored) { }

        return result;
    }

    List<String> find(String name) {
        List<String> result = new ArrayList<>();

        try {
            Statement statment = connection.createStatement();
            ResultSet resultSet = statment.executeQuery(String.format("SELECT * FROM products WHERE name='%s'", name));
            result = parseResult(resultSet);
        } catch (SQLException ignored) { }

        return result;
    }

    List<String> find(String minPrice, String maxPrice) {
        List<String> result = new ArrayList<>();

        try {
            Statement statment = connection.createStatement();
            ResultSet resultSet = statment.executeQuery(String.format("SELECT * FROM products WHERE price>%s AND price<%s", minPrice, maxPrice));
            result = parseResult(resultSet);
        } catch (SQLException ignored) { }

        return result;
    }

    void delete(String id) {
        try {
            Statement statment = connection.createStatement();
            statment.executeUpdate(String.format("DELETE FROM products WHERE id=%s", id));
        } catch (SQLException ignored) { }
    }

    private List<String> parseResult(ResultSet resultSet) {
        List<String> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(String.format("%s | %s | %s | %s",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getInt("amount")));
            }
        } catch (SQLException ignored) { }
        return result;
    }

    void close() { try { connection.close(); } catch (SQLException ignored) { } }

}