module com.tictactoe.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tictactoe.tictactoeclient to javafx.fxml;
    exports com.tictactoe.tictactoeclient;
}