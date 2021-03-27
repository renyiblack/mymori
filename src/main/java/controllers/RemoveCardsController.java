package controllers;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import utils.Dialogs;
import utils.Strings;
import models.Card;

import java.sql.SQLException;

public class RemoveCardsController extends Controller {
    @FXML
    private void initialize() {
        startGame();
    }

    public void clickEvent(ImageView imageView, Card card) {
        cardImage = imageView;

        grid.getChildren().remove(imageView);
        cards.remove(card);

        try {
            System.out.println(card.getId());
            CardDao cardDao = new CardDao();
            cardDao.delete(card.getId());
            Dialogs.showDialog(Strings.OK, Strings.DELETED_CARD);
        } catch (SQLException e) {
            Dialogs.showDialog(Strings.ERROR, Strings.ERROR_DELETE_CARD);
        }
    }
}
