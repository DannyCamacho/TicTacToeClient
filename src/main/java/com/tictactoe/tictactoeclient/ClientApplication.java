package com.tictactoe.tictactoeclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader titleScreenFxmlLoader= new FXMLLoader(ClientApplication.class.getResource("title-view.fxml"));
        Scene scene = new Scene(titleScreenFxmlLoader.load(), 600, 400);
        stage.setTitle("Tic-Tac-Toe!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}