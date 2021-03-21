package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EndDialogController {
    @FXML
    private Button goBack;

    @FXML
    private void goBackClicked() throws IOException {
        Stage dialog = (Stage) goBack.getScene().getWindow();
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Menu.fxml"));
        Stage stage = (Stage) dialog.getOwner().getScene().getWindow();
        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Mymori Game");
        stage.setScene(scene);
        stage.getScene().setRoot(root);

        root.setEffect(null);

        dialog.close();
    }
}
