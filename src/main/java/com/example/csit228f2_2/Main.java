package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) {
        createUsersTable();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("index.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createUsersTable(){
        try(Connection c = MySQLConnection.getConnection()){
            c.setAutoCommit(false);
            try(Statement statement = c.createStatement()){
                String createTableQueryUsers = "CREATE TABLE IF NOT EXISTS users ("+
                        "userId INT AUTO_INCREMENT PRIMARY KEY," +
                        "username VARCHAR(50) NOT NULL," +
                        "email VARCHAR(50) NOT NULL," +
                        "password VARCHAR(50) NOT NULL)";

                String createTableQueryLog = "CREATE TABLE IF NOT EXISTS tblLog ("+
                        "logId INT AUTO_INCREMENT PRIMARY KEY," +
                        "logTitle VARCHAR(255) NOT NULL," +
                        "logText VARCHAR(8000) NOT NULL," +
                        "userId INT," + // Foreign key
                        "FOREIGN KEY (userId) REFERENCES users(userId))"; // Foreign key constraint

                statement.execute(createTableQueryUsers);
                statement.execute(createTableQueryLog);


                c.commit();
                System.out.println("Tables created successfully!");
            }catch(SQLException e){
                c.rollback();
            }
        }catch(SQLException e){
            //e.printStackTrace();
        }catch(RuntimeException e){
           System.out.println("Error in Connection");
        }
    }




}
