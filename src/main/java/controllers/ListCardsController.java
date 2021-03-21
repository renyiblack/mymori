package controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import models.Card;

public class ListCardsController  extends Controller {
    public ListCardsController() {
        super();
    }

    @FXML
    private void initialize() {
        startGame();
    }

    @Override
    public void clickEvent(ImageView imageView, Card card) {
        card.setFlipped(!card.isFlipped());
        cardImage = imageView;
        Image image;

        if (card.isFlipped()) {
            image = card.getAnswer();
        } else {
            image = card.getQuestion();
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.4), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(-1);
        scaleTransition.play();
        scaleTransition.setOnFinished(event -> {
            imageView.setScaleX(1);
            imageView.setImage(image);
        });
    }
}