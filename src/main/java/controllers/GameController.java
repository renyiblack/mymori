package controllers;

import daos.CardDao;
import games.Game;
import games.MemoryGame;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utils.Dialogs;
import utils.Strings;
import models.Card;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class GameController extends Controller {
    @FXML
    private Label Moves, foundCardsLabel;
    @FXML
    private AnchorPane gamePane;
    Game game;

    public void setGame(Game game) {
        this.game = game;
        super.rows = 4;
        startGame();
    }

    @Override
    public void startGame() {
        setImageViews(game.imageViews);
        setCards();
        // shuffleCards();
        setImages();
        setAction();
    }

    @Override
    public void setAction() {
        if (game.answerCards.size() > 12) {
            Dialogs.showDialog(Strings.ERROR, Strings.MAX_CARDS_REACHED);
            CardDao cardDao = new CardDao();
            try {
                cardDao.delete(game.answerCards.get(game.answerCards.size() - 1).getId());
            } catch (SQLException e) {
                Dialogs.showDialog(Strings.ERROR, Strings.ERROR_DELETE_CARD);
            }
        } else if (game.answerCards.isEmpty() && game.imageViews.isEmpty())
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_LOAD_CARDS);
        else if (game.answerCards.size() < 12)
            Dialogs.showDialog(Strings.ERROR, Strings.TOO_FEW_CARDS);
        else
            for (int i = 0; i < game.imageViews.size(); i++) {
                final ImageView imageView = game.imageViews.get(i);
                Card card;
                if (i < 12)
                    card = game.answerCards.get(i);
                else
                    card = game.questionCards.get(i - 12);
                game.imageViews.get(i).setOnMouseClicked(event -> clickEvent(imageView, card));
            }
    }

    public void gameEnded() {
        game.gameEnded(backButton, gamePane);
    }

    @Override
    public void clickEvent(ImageView imageView, Card card) {
        game.gameRule(imageView, card, grid, backButton, gamePane);
        Moves.setText(Strings.MOVES + game.score.getMoves());
        foundCardsLabel.setText(Strings.FOUND_PAIRS + game.score.getFoundCards());
    }

    public void setCards() {
        CardDao cardDao = new CardDao();
        try {
            ArrayList<Card> dbCards = cardDao.getAll();
            for (Card card : dbCards) {
                game.answerCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(),
                        new Image(Strings.CARD_BACKGROUND)));
                game.questionCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(),
                        new Image(Strings.CARD_BACKGROUND)));
            }
        } catch (SQLException | NullPointerException e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_LOAD_CARDS);
        }
    }

    public void setImages() {
        if (game.answerCards.size() == 12)
            for (int i = 0; i < game.answerCards.size(); i++) {
                game.imageViews.get(i).setImage(game.answerCards.get(i).getBackground());

                game.questionCards.get(i).setAnswer(false);
                game.imageViews.get(i + 12).setImage(game.questionCards.get(i).getBackground());
            }
    }

    public void shuffleCards() {
        Collections.shuffle(game.imageViews);
    }
}
