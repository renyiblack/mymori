package controllers;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Card;
import utils.Dialogs;
import utils.Strings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    @FXML
    protected Button backButton;
    @FXML
    protected GridPane grid;
    public ArrayList<Card> cards;
    public ImageView cardImage;
    protected Integer rows, columns;
    protected Double height, width;
    public ArrayList<ImageView> imageViews;

    protected Controller() {
        rows = 2;
        columns = 6;
        height = 130.00;
        width = 90.00;
        cards = new ArrayList<>();
        imageViews = new ArrayList<>();
    }

    public void startGame() {
        setImageViews();
        setCards();
        setImages();
        setAction();
    }

    public void setAction() {
        if (cards.size() > imageViews.size()) {
            Dialogs.showDialog(Strings.ERROR, Strings.MAX_CARDS_REACHED);
            CardDao cardDao = new CardDao();
            try {
                cardDao.delete(cards.get(cards.size() - 1).getId());
            } catch (SQLException e) {
                Dialogs.showDialog(Strings.ERROR, Strings.ERROR_DELETE_CARD);
            }
        } else
            for (int i = 0; i < cards.size(); i++) {
                final ImageView imageView = imageViews.get(i);
                final Card card = cards.get(i);
                imageViews.get(i).setOnMouseClicked(event -> clickEvent(imageView, card));
            }
    }

    public void clickEvent(ImageView imageView, Card card) {
    }

    public void backClicked() {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Menu.fxml"));
            if (backButton != null && backButton.getScene() != null && backButton.getScene().getWindow() != null) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            Dialogs.showDialog("Error", "Couldn't go back to menu!");
        }
    }

    public void setImageViews() {
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(height);
            row.setMinHeight(Double.MIN_VALUE);
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(width);
            column.setMinWidth(Double.MIN_VALUE);
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                grid.add(imageView, j, i);
                imageViews.add(imageView);
            }
        }
    }

    public void setCards() {
        CardDao cardDao = new CardDao();
        try {
            ArrayList<Card> dbCards = cardDao.getAll();
            for (Card card : dbCards) {
                cards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(),
                        new Image("Images/Cards/background.png")));
            }
        } catch (SQLException e) {
            Dialogs.showDialog("Error", "Couldn't load cards!");
        }
    }

    public void setImages() {
        if (cards.size() <= imageViews.size())
            for (int i = 0; i < cards.size(); i++) {
                imageViews.get(i).setImage(cards.get(i).getQuestion());
            }
    }
}
