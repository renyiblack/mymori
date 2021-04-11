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
    public ImageView imageCard1, imageCard2;
    public Card card1, card2;
    public Boolean cardsMatched;
    private Integer clicks = 0;

    public Integer idCard1, idCard2;

    public MemoryGame() {
        cardsMatched = false;
        foundCards = new ArrayList<>();
    }

    @Override
    public void gameRule(ImageView imageView, Card card, GridPane grid, Button backButton, AnchorPane gamePane) {
        cardsMatched = false;
        clicks++;
        imageView.setDisable(true);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.4), imageView);
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

            disableAll();

            // If cards match, save them and disable
            if (idCard1.equals(idCard2)) {
                score.updateFoundCards();

                cardsMatched = true;

                foundCards.add(imageCard1);
                foundCards.add(imageCard2);

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.6), event -> {
                    imageCard1.setDisable(true);
                    imageCard2.setDisable(true);
                    imageCard1.setOpacity(0.6);
                    imageCard2.setOpacity(0.6);
                    enableAll();
                }));
                timeline.play();
            } else { // If cards don't match, turn them back
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), event -> {
                    imageCard1.setImage(card1.getBackground());
                    imageCard2.setImage(card2.getBackground());
                    imageCard1.setDisable(false);
                    imageCard2.setDisable(false);
                    enableAll();
                }));
                timeline.play();
            }

            if (foundCards.size() == 24) {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), event -> deleteCards(grid)));
                timeline.play();

                this.gameEnded(backButton, gamePane);
            }
            clicks = 0;
        }
    }

    @Override
    public void gameEnded(Button backButton, AnchorPane gamePane) {
        try {
            Dialogs.showImportantDialog(backButton, gamePane, Strings.END_DIALOG_FXML, Strings.END_MEMORY_GAME_DIALOG_TITLE);
        } catch (IOException e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_END_GAME);
        }
    }
}
