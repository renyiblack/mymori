package games;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Card;

import java.util.ArrayList;
import java.util.Collections;

public class SequenceGame extends Game {
    public ArrayList<ImageView> imageCards;
    public ArrayList<Card> shuffleCards;
    public ArrayList<Card> selectedCards;

    private Integer clicks = -1;
    private Integer level = 0;
    private Boolean lvlStarted = true;

    public SequenceGame() {
        foundCards = new ArrayList<>();
        shuffleCards = new ArrayList<>();
    }

    @Override
    public void gameRule(ImageView imageView, Card card, GridPane grid, Button backButton, AnchorPane gamePane) {
        shuffleCards();

        clicks++;
        if (clicks > 0) {
            flip(imageView, card);

            if (card.getId() == shuffleCards.get(clicks - 1).getId()) {
                score.updateFoundCards();

                foundCards.add(imageView);
            } else {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), event -> deleteCards(grid)));
                timeline.play();

                super.gameEnded(backButton, gamePane);
            }

            if (clicks == shuffleCards.size() && clicks < 24) {
                for (ImageView iv : foundCards) {
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), event -> {
                        iv.setImage(shuffleCards.get(0).getBackground());
                    }));
                    timeline.play();
                }
                clicks = -1;
                lvlStarted = true;
            } else if (clicks == shuffleCards.size() && clicks >= 24) {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), event -> deleteCards(grid)));
                timeline.play();

                super.gameEnded(backButton, gamePane);
            }
        }
    }

    private void flip(ImageView imageView, Card card) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), imageView);
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

    private void unflip(ImageView imageView, Card card, double s) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(s), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(-1);
        scaleTransition.play();
        scaleTransition.setOnFinished(event -> {
            imageView.setScaleX(1);
            imageView.setImage(card.getBackground());
        });
    }

    private void shuffleCards() {
        if (lvlStarted) {
            lvlStarted = false;
            ArrayList<Card> tempCards = new ArrayList<>();
            ArrayList<Card> oldTempCards = new ArrayList<>();
            oldTempCards.addAll(questionCards);
            oldTempCards.addAll(answerCards);
            tempCards.addAll(questionCards);
            tempCards.addAll(answerCards);
            Collections.shuffle(tempCards);
            shuffleCards.clear();

            if (level + 2 <= 24) {
                level += 2;
                shuffleCards.addAll(tempCards.subList(0, level));

                double s = 1;

                for (Card card : shuffleCards) {
                    int i = oldTempCards.indexOf(card);
                    flip(imageViews.get(imageViews.indexOf(oldImageViews.get(i))), card);
                }

                for (Card card : shuffleCards) {
                    int i = oldTempCards.indexOf(card);
                    unflip(imageViews.get(imageViews.indexOf(oldImageViews.get(i))), card, s);
                    s += 0.5;
                }
            }
        }
    }
}
