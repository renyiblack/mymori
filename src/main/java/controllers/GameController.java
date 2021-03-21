package controllers;

import daos.CardDao;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import utils.Strings;
import models.Card;
import models.Controller;
import models.Score;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class GameController extends Controller {
    @FXML
    private Label Moves, foundCardsLabel;
    @FXML
    private AnchorPane game;

    public ImageView imageCard1, imageCard2;
    public ArrayList<ImageView> foundCards;
    public ArrayList<Card> answerCards, questionCards;
    public Card card1, card2;

    public Score score;
    public Boolean cardsMatched;
    public Integer idCard1, idCard2;

    private Integer clicks = 0;

    public GameController() {
        super();
        super.rows = 4;
        answerCards = new ArrayList<>();
        questionCards = new ArrayList<>();
        foundCards = new ArrayList<>();
        score = new Score();
        cardsMatched = false;
    }

    @FXML
    private void initialize() {
        startGame();
    }

    @Override
    public void startGame() {
        createImageViews();
        createCards();
        shuffleCards();
        setImages();
        player();
    }

    @Override
    public void player() {
        if (answerCards.size() > 12) {
            showDialog(Strings.ERROR, Strings.MAX_CARDS_REACHED);
            CardDao cardDao = new CardDao();
            try {
                cardDao.delete(answerCards.get(answerCards.size() - 1).getId());
            } catch (SQLException e) {
                showDialog(Strings.ERROR, Strings.ERROR_DELETE_CARD);
            }
        } else if (answerCards.isEmpty() && imageViews.isEmpty())
            showDialog(Strings.ERROR, Strings.ERROR_LOAD_CARDS);
        else if (answerCards.size() < 12)
            showDialog(Strings.ERROR, Strings.TOO_FEW_CARDS);
        else
            for (int i = 0; i < imageViews.size(); i++) {
                final ImageView imageView = imageViews.get(i);
                Card card;
                if (i < 12)
                    card = answerCards.get(i);
                else
                    card = questionCards.get(i - 12);
                imageViews.get(i).setOnMouseClicked(event -> clickEvent(imageView, card));
            }
    }

    public void clickEvent(ImageView imageView, Card card) {
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
        } else if (clicks == 2) {  // Selected second card
            idCard2 = card.getId();
            imageCard2 = imageView;
            card2 = card;

            score.updateMoves();

            Moves.setText(Strings.MOVES + score.getMoves());
            disableAll();

            // If cards match, save them and disable
            if (idCard1.equals(idCard2)) {
                score.updateFoundCards();

                foundCardsLabel.setText(Strings.FOUND_PAIRS + score.getFoundCards());
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

                gameEnded();
            }
            clicks = 0;
        }
    }

    public void gameEnded() {
        try {
            showImportantDialog(backButton, game, Strings.END_DIALOG_FXML, Strings.END_DIALOG_TITLE);
        } catch (Exception e) {
            showDialog(Strings.ERROR, Strings.ERROR_END_GAME);
        }
    }

    public void deleteCards(GridPane grid) {
        for (ImageView imageView : imageViews) {
            grid.getChildren().remove(imageView);
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

    public void disableAll() {
        for (ImageView imageView : imageViews) {
            imageView.setDisable(true);
        }
    }

    public void createCards() {
        CardDao cardDao = new CardDao();
        try {
            ArrayList<Card> dbCards = cardDao.getAll();
            for (Card card : dbCards) {
                answerCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(), new Image(Strings.CARD_BACKGROUND)));
                questionCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(), new Image(Strings.CARD_BACKGROUND)));
            }
        } catch (SQLException e) {
            showDialog(Strings.ERROR, Strings.ERROR_LOAD_CARDS);
        }
    }

    public void setImages() {
        if (answerCards.size() == 12)
            for (int i = 0; i < answerCards.size(); i++) {
                imageViews.get(i).setImage(answerCards.get(i).getBackground());

                questionCards.get(i).setAnswer(false);
                imageViews.get(i + 12).setImage(questionCards.get(i).getBackground());
            }
    }

    public void shuffleCards() {
        Collections.shuffle(imageViews);
    }
}
