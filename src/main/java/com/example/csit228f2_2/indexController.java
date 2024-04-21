package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class indexController {

    @FXML
    public Button btnRegister;

    @FXML
    private void onBtnRegisterClick() throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }


}
