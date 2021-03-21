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
import utils.Strings;
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
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(Strings.GAME_FXML));
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    private void exitClicked() {
        try {
            Controller.showImportantDialog(exitButton, menu, Strings.EXIT_DIALOG_FXML, Strings.EXIT_DIALOG_TITLE);
        } catch (Exception e) {
            Controller.showDialog(Strings.ERROR, Strings.ERROR_CLOSE_GAME);
        }
    }

    @FXML
    private void addCardClicked() {
        File questionChooser, answerChooser;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files(*.png)", "*.png"));
        fileChooser.setTitle(Strings.QUESTION_CHOOSER_TITLE);
        questionChooser = fileChooser.showOpenDialog(null);
        fileChooser.setTitle(Strings.ANSWER_CHOOSER_TITLE);
        answerChooser = fileChooser.showOpenDialog(null);

        if (questionChooser == null || answerChooser == null) {
            Controller.showDialog(Strings.ERROR, Strings.INVALID_QUESTION_ANSWER);
        } else {
            Image question = new Image(questionChooser.toURI().toString());
            Image answer = new Image(answerChooser.toURI().toString());
            if ((question.getHeight() != 726 && question.getWidth() != 500) || (answer.getHeight() != 726 && answer.getWidth() != 500)) {
                Controller.showDialog(Strings.ERROR, Strings.INVALID_QUESTION_ANSWER_SIZE);
            } else {
                CardDao cardDao = new CardDao();
                try {
                    ArrayList<Card> cards = cardDao.getAll();
                    if (cards.size() < 12) {
                        cardDao.insert(new Card(0, question, answer, new Image(Strings.CARD_BACKGROUND)));
                        Controller.showDialog(Strings.OK, Strings.SAVED_CARD);
                    } else
                        Controller.showDialog(Strings.ERROR, Strings.MAX_CARDS_REACHED);
                } catch (SQLException e) {
                    Controller.showDialog(Strings.ERROR, Strings.ERROR_SAVE_CARD);
                }
            }
        }
    }

    public void listCardsClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(Strings.LIST_CARDS_FXML));
        Stage stage = (Stage) listCardsButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void removeCardsClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(Strings.REMOVE_CARDS_FXML));
        Stage stage = (Stage) removeCardsButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}