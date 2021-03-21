package controllers;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import models.Card;
import models.Controller;

import java.sql.SQLException;

public class RemoveCardsController extends Controller {
    public RemoveCardsController() {
        super();
    }

    @FXML
    private void initialize() {
        startGame();
    }

    public void clickEvent(ImageView imageView, Card card) {
        cardImage = imageView;

        grid.getChildren().remove(imageView);
        cards.remove(card);

        CardDao cardDao = new CardDao();
        try {
            cardDao.delete(card.getId());
        } catch (SQLException e) {
            showDialog("Error", "Couldn't delete card!");
        }
    }
}
