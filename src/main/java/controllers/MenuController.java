package controllers;

import daos.CardDao;
import games.GroupGame;
import games.MemoryGame;
import games.SequenceGame;
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
import utils.draw.Drawing;
import utils.Dialogs;
import utils.Strings;
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
    public Button drawCardButton;
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

        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource(Strings.GAME_FXML));

        Parent root = (Parent) fxmlLoader.load();

        GameController controller = fxmlLoader.<GameController>getController();
        // controller.setGame(new MemoryGame());
        // controller.setGame(new GroupGame());
        controller.setGame(new SequenceGame());
        
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    private void exitClicked() {
        try {
            Dialogs.showImportantDialog(exitButton, menu, Strings.EXIT_DIALOG_FXML, Strings.EXIT_DIALOG_TITLE);
        } catch (Exception e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_CLOSE_GAME);
        }
    }

    @FXML
    private void drawCardClicked() {
        Drawing drawing = new Drawing();
        drawing.draw();
    }

    @FXML
    private void addCardClicked() {
        File questionChooser, answerChooser;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files(*.png)", "*.png"));
        fileChooser.setTitle(Strings.QUESTION_CHOOSER_TITLE);
        Dialogs.showDialog(Strings.CARD_CHOOSER, Strings.QUESTION_CHOOSER);
        questionChooser = fileChooser.showOpenDialog(null);
        Dialogs.showDialog(Strings.CARD_CHOOSER, Strings.ANSWER_CHOOSER);
        fileChooser.setTitle(Strings.ANSWER_CHOOSER_TITLE);
        answerChooser = fileChooser.showOpenDialog(null);

        if (questionChooser == null || answerChooser == null) {
            Dialogs.showDialog(Strings.ERROR, Strings.INVALID_QUESTION_ANSWER);
        } else {
            Image question = new Image(questionChooser.toURI().toString());
            Image answer = new Image(answerChooser.toURI().toString());
            if ((question.getHeight() != 726 && question.getWidth() != 500)
                    || (answer.getHeight() != 726 && answer.getWidth() != 500)) {
                Dialogs.showDialog(Strings.ERROR, Strings.INVALID_QUESTION_ANSWER_SIZE);
            } else {
                CardDao cardDao = new CardDao();
                try {
                    ArrayList<Card> cards = cardDao.getAll();
                    if (cards.size() < 12) {
                        cardDao.insert(new Card(0, question, answer, new Image(Strings.CARD_BACKGROUND)));
                        Dialogs.showDialog(Strings.OK, Strings.SAVED_CARD);
                    } else
                        Dialogs.showDialog(Strings.ERROR, Strings.MAX_CARDS_REACHED);
                } catch (SQLException e) {
                    Dialogs.showDialog(Strings.ERROR, Strings.ERROR_SAVE_CARD);
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
