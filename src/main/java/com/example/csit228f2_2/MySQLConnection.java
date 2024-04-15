package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/laderadb";
    private static final String USERNAME = "Theo";
    private static final String PASSWORD = "123456";
    public static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the Database!");
        }catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
        return c;
    }
}
