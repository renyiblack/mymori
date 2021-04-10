package games;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Card;
import models.Score;
import utils.Dialogs;
import utils.Strings;

public class Game {
    public ArrayList<Card> answerCards, questionCards;
    public ArrayList<ImageView> foundCards;
    public ArrayList<ImageView> imageViews;
    public Score score;

    Game() {
        score = new Score();
        imageViews = new ArrayList<>();
        answerCards = new ArrayList<>();
        questionCards = new ArrayList<>();
    }

    public void gameRule(ImageView imageView, Card card, GridPane grid, Button backButton, AnchorPane gamePane) {
    }

    public void disableAll() {
        for (ImageView imageView : imageViews) {
            imageView.setDisable(true);
        }
    }

    public void enableAll() {
        for (ImageView imageView : imageViews) {
            imageView.setDisable(false);
        }

        for (ImageView foundCard : foundCards) {
            foundCard.setDisable(true);
        }
    }

    public void gameEnded(Button backButton, AnchorPane gamePane) {
        try {
            Dialogs.showImportantDialog(backButton, gamePane, Strings.END_DIALOG_FXML, Strings.END_DIALOG_TITLE);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_END_GAME);
        }
    }

    public void deleteCards(GridPane grid) {
        for (ImageView imageView : imageViews) {
            grid.getChildren().remove(imageView);
        }
    }
}