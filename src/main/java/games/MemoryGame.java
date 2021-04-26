package games;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Card;
import utils.Dialogs;
import utils.Strings;

import java.io.IOException;
import java.util.ArrayList;

public class MemoryGame extends Game {
    public Integer idCard1, idCard2;
    public ImageView imageCard1, imageCard2;
    public Card card1, card2;
    public Boolean cardsMatched;
    private Integer clicks;

    public MemoryGame() {
        this.animationTime = 0.6;
        this.clicks = 0;
        this.cardsMatched = false;
        this.foundCardsViews = new ArrayList<>();
    }

    public void flipCard(ImageView imageView, Card card) {
        imageView.setDisable(true);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(this.animationTime), imageView);

        scaleTransition.setFromX(1);
        scaleTransition.setToX(-1);

        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            imageView.setScaleX(1);
            if (card.getIsAnswer())
                imageView.setImage(card.getAnswer());
            else
                imageView.setImage(card.getQuestion());
        });
    }

    @Override
    public void gameRule(ImageView imageView, Card card, GridPane grid, Button backButton, AnchorPane gamePane) {
        cardsMatched = false;
        clicks++;

        flipCard(imageView, card);

        // Selected first card
        if (clicks == 1) {
            idCard1 = card.getId();
            imageCard1 = imageView;
            card1 = card;
        } else if (clicks == 2) { // Selected second card
            idCard2 = card.getId();
            imageCard2 = imageView;
            card2 = card;

            score.updateMoves();

            disableAllCards();

            // If cards match, save them and disable
            if (idCard1.equals(idCard2)) {
                score.updateFoundCards();

                cardsMatched = true;

                foundCardsViews.add(imageCard1);
                foundCardsViews.add(imageCard2);

                super.selectCard(imageCard1, true, true);
                super.selectCard(imageCard2, true, true);
            } else { // If cards don't match, turn them back
                super.selectCard(imageCard1, false, true);
                super.selectCard(imageCard2, false, true);
            }

            if (foundCardsViews.size() == 24) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(this.animationTime * 2), event -> deleteCards(grid)));
                timeline.play();

                this.endNotification(backButton, gamePane);
            }
            clicks = 0;
        }
    }

    @Override
    public void endNotification(Button backButton, AnchorPane gamePane) {
        try {
            Dialogs.showImportantDialog(backButton, gamePane, Strings.END_DIALOG_FXML,
                    Strings.END_MEMORY_GAME_DIALOG_TITLE);
        } catch (IOException e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_END_GAME);
        }
    }
}
