package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ExitDialogController {
    @FXML
    private Button yes, no;

    @FXML
    private Label exit;

    @FXML
    private void yesClicked() {
        Platform.exit();
    }

    @FXML
    private void noClicked() {
        Stage dialog = (Stage) no.getScene().getWindow();

        Parent root = dialog.getOwner().getScene().getRoot();
        root.setEffect(null);

        dialog.close();
    }
}