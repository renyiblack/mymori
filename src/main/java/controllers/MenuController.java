package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MenuController {
    @FXML
    public VBox vbox;
    @FXML
    private Button exitButton, playButton;
    @FXML
    private ImageView cards;
    @FXML
    private AnchorPane menu;

    @FXML
    private void initialize() {
        cards.setFitWidth(531);
        cards.setFitHeight(205);
    }

    @FXML
    private void playClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Game.fxml"));
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    private void exitClicked() throws Exception {
        Stage primaryStage = (Stage) exitButton.getScene().getWindow();
        GaussianBlur blur = new GaussianBlur(3);
        menu.setEffect(blur);

        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("ExitDialog.fxml"));

        Stage dialog = new Stage();
        dialog.setTitle("Exit");

        Scene scene = new Scene(root, 425, 200);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);

        double centerXPosition = primaryStage.getX() + primaryStage.getWidth() / 2d;
        double centerYPosition = primaryStage.getY() + primaryStage.getHeight() / 2d;

        dialog.setOnShowing(event -> dialog.hide());

        dialog.setOnShown(event -> {
            dialog.setX(centerXPosition - dialog.getWidth() / 2d);
            dialog.setY(centerYPosition - dialog.getHeight() / 2d);
            dialog.show();
        });

        dialog.show();
    }
}
