package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
             Statement statement = c.createStatement()) {

            String selectQuery = "SELECT logTitle FROM tblLog";
            ResultSet resultSet = statement.executeQuery(selectQuery);

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
    public TextArea taStaticLogContent;


    @FXML
    public Text txtName;

    private void initialize(){
        if (txtName != null && chbLogTitle!= null) {
            txtName.setText(currUsername);
            chbLogTitle.setOnAction(actionEvent -> {
                String selectedOption = chbLogTitle.getValue();
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "SELECT logText FROM tblLog WHERE logTitle = ?"
                     )) {
                    statement.setString(1, selectedOption);
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
        initialize();
    }
}
