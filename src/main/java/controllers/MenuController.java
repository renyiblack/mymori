package controllers;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Card;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuController {
    @FXML
    public VBox vbox;
    @FXML
    public Button addCardButton;
    @FXML
    public Button listCardsButton;
    @FXML
    public Button removeCardsButton;
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

    private void showDialog(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addCardClicked() {
        File questionChooser, answerChooser;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files(*.png)", "*.png"));
        fileChooser.setTitle("Question Chooser");
        questionChooser = fileChooser.showOpenDialog(null);
        fileChooser.setTitle("Answer Chooser");
        answerChooser = fileChooser.showOpenDialog(null);

        if (questionChooser == null || answerChooser == null) {
            showDialog("Error", "Invalid Question or Answer!");
        } else {
            Image question = new Image(questionChooser.toURI().toString());
            Image answer = new Image(answerChooser.toURI().toString());
            if ((question.getHeight() != 726 && question.getWidth() != 500) || (answer.getHeight() != 726 && answer.getWidth() != 500)) {
                showDialog("Error", "Question or Answer is not a valid image (500x726)");
            } else {
                CardDao cardDao = new CardDao();
                try {
                    ArrayList<Card> cards = cardDao.getAll();
                    if(cards.size() < 12) {
                        cardDao.insert(new Card(0, question, answer, new Image("Images/Cards/backgroundSmall.png")));
                        showDialog("Ok", "Card successfully saved!");
                    }
                    else
                        showDialog("Error", "Game can only have 12 cards!");
                } catch (SQLException e) {
                    showDialog("Error", "Couldn't save card!");
                }
            }
        }
    }

    public void listCardsClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("ListCards.fxml"));
        Stage stage = (Stage) listCardsButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void removeCardsClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("RemoveCards.fxml"));
        Stage stage = (Stage) removeCardsButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
