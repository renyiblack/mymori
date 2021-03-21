package controllers;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Card;
import models.Controller;

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
    private void exitClicked() {
        try {
            Controller.showImportantDialog(exitButton, menu, "ExitDialog.fxml", "Exit");
        } catch (Exception e) {
            Controller.showDialog("Error", "Couldn't close game!");
        }
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
            Controller.showDialog("Error", "Invalid Question or Answer!");
        } else {
            Image question = new Image(questionChooser.toURI().toString());
            Image answer = new Image(answerChooser.toURI().toString());
            if ((question.getHeight() != 726 && question.getWidth() != 500) || (answer.getHeight() != 726 && answer.getWidth() != 500)) {
                Controller.showDialog("Error", "Question or Answer is not a valid image (500x726)");
            } else {
                CardDao cardDao = new CardDao();
                try {
                    ArrayList<Card> cards = cardDao.getAll();
                    if (cards.size() < 12) {
                        cardDao.insert(new Card(0, question, answer, new Image("Images/Cards/backgroundSmall.png")));
                        Controller.showDialog("Ok", "Card successfully saved!");
                    } else
                        Controller.showDialog("Error", "Game can only have 12 cards!");
                } catch (SQLException e) {
                    Controller.showDialog("Error", "Couldn't save card!");
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
