package games;

import java.io.IOException;
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
    public ArrayList<ImageView> foundCardsViews;
    public ArrayList<ImageView> cardsViews;
    public ArrayList<ImageView> oldImageViews;
    public Score score;
    public Double animationTime;

    Game() {
        score = new Score();
        cardsViews = new ArrayList<>();
        answerCards = new ArrayList<>();
        questionCards = new ArrayList<>();
    }

    public void gameRule(ImageView imageView, Card card, GridPane grid, Button backButton, AnchorPane gamePane) {
    }

    public void disableAllCards() {
        for (ImageView imageView : cardsViews) {
            imageView.setDisable(true);
        }
    }

    public void enableAllCards() {
        for (ImageView imageView : cardsViews) {
            imageView.setDisable(false);
        }

        for (ImageView foundCard : foundCardsViews) {
            foundCard.setDisable(true);
        }
    }

    public void endNotification(Button backButton, AnchorPane gamePane) {
        try {
            Dialogs.showImportantDialog(backButton, gamePane, Strings.END_DIALOG_FXML, Strings.END_DIALOG_TITLE);
        } catch (IOException e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_END_GAME);
        }
    }

    public void deleteCards(GridPane grid) {
        for (ImageView imageView : cardsViews) {
            grid.getChildren().remove(imageView);
        }
    }
}