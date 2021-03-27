package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class Dialogs {
    public static void showDialog(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showImportantDialog(Button button, AnchorPane anchorPane, String FXML, String title)
            throws Exception {
        Stage primaryStage = (Stage) button.getScene().getWindow();
        GaussianBlur blur = new GaussianBlur(3);
        anchorPane.setEffect(blur);

        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(FXML));

        Stage dialog = new Stage();
        dialog.setTitle(title);

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
