package com.tictactoe.tictactoeclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class TitleController {
    @FXML
    public Button localGameButton, onlineGameButton, quitButton;
    public Label titleScreenLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void localGameButtonClicked() {
        localGameButton.setVisible(false);
        onlineGameButton.setVisible(false);
        quitButton.setVisible(false);
        titleScreenLabel.setText("Select a Difficulty");
    }

    @FXML
    public void onlineGameButtonClicked(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lobby-view.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void quitButtonClicked() {
        Platform.exit();
        System.exit(0);
    }
}