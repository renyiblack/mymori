package controllers;

import daos.CardDao;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Card;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListCardsController {
    @FXML
    private Button back;
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane game;

    public ArrayList<ImageView> imageViews;
    public ImageView cardImage;
    public ArrayList<Card> cards;

    public ListCardsController() {
        imageViews = new ArrayList<>();
        cards = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        startGame();
    }

    public void startGame() {
        createImageViews(grid, imageViews);
        createCards(cards);
        setImages(imageViews, cards);
        player();
    }

    public void player() {
        if (cards.size() > imageViews.size()) {
            showDialog("Error", "Game can only have 12 cards! Removing last added one.");
            CardDao cardDao = new CardDao();
            try {
                cardDao.delete(cards.get(cards.size() - 1).getId());
            } catch (SQLException e) {
                showDialog("Error", "Couldn't delete last card.");
            }
        } else
            for (int i = 0; i < cards.size(); i++) {
                final ImageView imageView = imageViews.get(i);
                final Card card = cards.get(i);
                imageViews.get(i).setOnMouseClicked(event -> clickEvent(imageView, card));
            }
    }

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

    public void backClicked() {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Menu.fxml"));
            if (back != null && back.getScene() != null && back.getScene().getWindow() != null) {
                Stage stage = (Stage) back.getScene().getWindow();
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            showDialog("Error", "Couldn't go back to menu!");
        }
    }

    public void createImageViews(GridPane grid, ArrayList<ImageView> imageViews) {
        grid.setHgap(10);
        grid.setVgap(10);

        int rows = 2;
        double height = 130;
        int columns = 6;
        double width = 90;

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

    private void showDialog(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void createCards(ArrayList<Card> cards) {
        CardDao cardDao = new CardDao();
        try {
            ArrayList<Card> dbCards = cardDao.getAll();
            for (Card card : dbCards) {
                cards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(), new Image("Images/Cards/backgroundSmall.png")));
            }
        } catch (SQLException e) {
            showDialog("Error", "Couldn't load cards!");
        }
    }

    public void setImages(ArrayList<ImageView> imageViews, ArrayList<Card> cards) {
        if (cards.size() <= imageViews.size())
            for (int i = 0; i < cards.size(); i++) {
                imageViews.get(i).setImage(cards.get(i).getQuestion());
            }
    }
}
