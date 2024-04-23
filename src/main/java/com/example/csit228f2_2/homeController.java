package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class homeController implements Initializable {

    private int UserId;
    private String currUsername;

    public void initializeUserData(int userId, String username) {
        this.UserId = userId;
        this.currUsername = username;
    }

    @FXML
    public TextField tfLogTitle;

    @FXML
    public TextArea taLogContent;

    @FXML
    private void insertLog() {
        String logTitle = tfLogTitle.getText();
        String logText = taLogContent.getText();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "INSERT INTO tblLog (logTitle, logText, userId) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, logTitle);
            preparedStatement.setString(2, logText);
            preparedStatement.setInt(3, UserId);


            if(Objects.equals(logTitle, "") || Objects.equals(logText, "") ){
                System.out.println("Failed to insert log!");
                return;
            }

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Log inserted successfully!");
                populateLogTitles();
                clearFields();
            } else {
                System.out.println("Failed to insert log!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void updateLog() {
        String logTitle = tfLogTitle.getText();
        String logText = taLogContent.getText();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "UPDATE tblLog SET logTitle = ?, logText = ? WHERE userId = ?")) {
            preparedStatement.setString(1, logTitle);
            preparedStatement.setString(2, logText);
            preparedStatement.setInt(3, UserId);

            if(Objects.equals(logTitle, "") || Objects.equals(logText, "") ){
                System.out.println("Failed to update log!");
            }

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Log updated successfully!");
                populateLogTitles();
                clearFields();
            } else {
                System.out.println("Failed to update log!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public ChoiceBox<String> chbLogTitle;




    @FXML
    private void deleteLog() {
        String logTitle = chbLogTitle.getValue();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "DELETE FROM tblLog WHERE logTitle = ? AND userId = ?")) {

            preparedStatement.setString(1, logTitle);
            preparedStatement.setInt(2, UserId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Log deleted successfully!");
                populateLogTitles();
                clearFields();
            } else {
                System.out.println("Failed to delete log!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateLogTitles() {
        if (chbLogTitle != null) {
            chbLogTitle.getItems().clear();
        }

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "SELECT logTitle FROM tblLog WHERE userId = ?")) {

            preparedStatement.setInt(1, UserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String logTitle = resultSet.getString("logTitle");
                if(chbLogTitle != null){
                    chbLogTitle.getItems().add(logTitle);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tfLogTitle.clear();
        taLogContent.clear();
    }


    @FXML
    public Button btnResetPass;

    @FXML
    public Button btnDelAccount;

    @FXML
    public BorderPane bpProfile;

    @FXML
    private void changePaneToResetPass() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forgotPass.fxml"));
        Pane tempPane = fxmlLoader.load();
        bpProfile.setCenter(tempPane);
    }

    @FXML
    public Button btnProfile;

    @FXML
    private void changeToProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        loader.setControllerFactory(controllerClass -> {
            homeController controller = new homeController();
            controller.initializeUserData(UserId,currUsername);
            return controller;
        });
        Parent pane = loader.load();
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
        Scene scene = new Scene(pane, currentWidth, currentHeight);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void changeToDelete() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("deleteAccount.fxml"));
        Pane tempPane = fxmlLoader.load();
        bpProfile.setCenter(tempPane);
    }

    @FXML
    public TextField tfemail;

    @FXML
    public TextField tfpassword;

    @FXML
    public Button btnConfirmDel;

    @FXML
    private void changePassword() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "UPDATE users SET password = ? WHERE email = ?")) {

            String email = tfemail.getText();
            String pass = tfpassword.getText();

            preparedStatement.setString(2, email);
            preparedStatement.setString(1, pass);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Account Password Changed Succesfully");
                tfemail.clear();
                tfpassword.clear();
            } else {
                System.out.println("Account Password change FAIL ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAccount() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "DELETE FROM users WHERE email = ? AND password = ?")) {

            String email = tfemail.getText();
            String pass = tfpassword.getText();

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {

                System.out.println("Account Deleted Succesfully");

                Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
                Stage stage = (Stage) btnConfirmDel.getScene().getWindow();
                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                Scene scene = new Scene(pane, currentWidth, currentHeight);
                stage.setScene(scene);
                stage.show();

            } else {
                System.out.println("Account Deletion FAIL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public TextArea taStaticLogContent;

    @FXML
    public Button btnProfileBack;

    @FXML
    private void goBackToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        loader.setControllerFactory(controllerClass -> {
            homeController controller = new homeController();
            controller.initializeUserData(UserId,currUsername);
            return controller;
        });
        Parent pane = loader.load();
        Stage stage = (Stage) btnProfileBack.getScene().getWindow();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
        Scene scene = new Scene(pane, currentWidth, currentHeight);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public Text txtName;

    private void initialize(){
        if (txtName != null && chbLogTitle!= null) {
            txtName.setText(currUsername);
            chbLogTitle.setOnAction(actionEvent -> {
                String selectedOption = chbLogTitle.getValue();
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "SELECT logText FROM tblLog WHERE logTitle = ? AND userId = ?"
                     )) {
                    statement.setString(1, selectedOption);
//                    System.out.println(UserId);
                    statement.setInt(2, UserId);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String logText = resultSet.getString("logText");
                        taStaticLogContent.setText(logText);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        populateLogTitles();




    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("USERID: " + UserId + "Username: " + currUsername);
        initialize();
    }
}
