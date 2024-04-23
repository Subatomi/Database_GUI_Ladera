package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class indexController {

    private int UserId;
    private String currUsername;

    @FXML
    public Button btnRegister;

    @FXML
    public Button btnRegBack;
    @FXML
    private void onBtnRegisterClick() throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
        Scene scene = new Scene(pane, currentWidth, currentHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onBtnRegGoBack() throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
        Stage stage = (Stage) btnRegBack.getScene().getWindow();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
        Scene scene = new Scene(pane, currentWidth, currentHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onUsersUpdate(){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET name = ?, email = ? WHERE id = ?")) {
            String newName = "Jane Doe";
            String newEmail = "jane@example.com";
            int userIdToUpdate = 1;

            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newEmail);
            preparedStatement.setInt(3, userIdToUpdate);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public TextField tfUsername;

    @FXML
    public TextField tfPassword;

    @FXML
    public TextField tfEmail;

    @FXML
    public Button btnLogIn;

    @FXML
    public Button btnRegRegister;

    @FXML
    private void insertDataUsers(){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement checkStatement = c.prepareStatement("SELECT username FROM users WHERE username = ?");
             PreparedStatement insertStatement = c.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            String name = tfUsername.getText();
            String email = tfEmail.getText();
            String pass = tfPassword.getText();

            checkStatement.setString(1, name);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Username already exists!");

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Username Entry!");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();

                return;
            }

            insertStatement.setString(1, name);
            insertStatement.setString(2, email);
            insertStatement.setString(3, pass);

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data Inserted Successfully!");
                Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
                Stage stage = (Stage) btnRegRegister.getScene().getWindow();
                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                Scene scene = new Scene(pane, currentWidth, currentHeight);
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onLogInCheck() {
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){

            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            String username = tfUsername.getText();
            String password = tfPassword.getText();

            while(resultSet.next()){
                String name = resultSet.getString("username");
                String pass = resultSet.getString("password");

                if(Objects.equals(username, name) && Objects.equals(password, pass)){

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                    loader.setControllerFactory(controllerClass -> {
                        homeController controller = new homeController();
                        try {
                            controller.initializeUserData(resultSet.getInt("userId"), resultSet.getString("username"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return controller;
                    });

                    Parent pane = loader.load();
                    Stage stage = (Stage) btnLogIn.getScene().getWindow();
                    double currentWidth = stage.getWidth();
                    double currentHeight = stage.getHeight();
                    Scene scene = new Scene(pane, currentWidth, currentHeight);
                    stage.setScene(scene);
                    stage.show();

                    return;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


