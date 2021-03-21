package controllers;

import daos.CardDao;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.Card;
import models.Score;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class GameController {
    @FXML
    private Button back;
    @FXML
    private GridPane grid;
    @FXML
    private Label Moves, foundCardsLabel;
    @FXML
    private AnchorPane game;

    public ArrayList<ImageView> imageViews, foundCards;
    public ImageView imageCard1, imageCard2;
    public Card card1, card2;
    public ArrayList<Card> answerCards, questionCards;
    public Score score;
    public Boolean cardsMatched;
    public int id1, id2;

    private int clicks = 0;

    public GameController() {
        imageViews = new ArrayList<>();
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

    public void startGame() {
        createImageViews();
        createCards();
        shuffleCards();
        setImages();

        player();
    }

    public void player() {
        if (answerCards.size() > imageViews.size()) {
            showDialog("Error", "Game can only have 12 cards! Removing last added one.");
            CardDao cardDao = new CardDao();
            try {
                cardDao.delete(answerCards.get(answerCards.size() - 1).getId());
            } catch (SQLException e) {
                showDialog("Error", "Couldn't delete last card.");
            }
        } else
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


        if (clicks == 1) {
            id1 = card.getId();
            imageCard1 = imageView;
            card1 = card;
        } else if (clicks == 2) {
            id2 = card.getId();
            imageCard2 = imageView;
            card2 = card;
            score.updateMoves();
            String moves = "Moves: ";
            Moves.setText(moves + score.getMoves());
            disableAll();
            if (id1 == id2) {
                score.updateFoundCards();
                String foundPairs = "Found Pairs: ";
                foundCardsLabel.setText(foundPairs + score.getFoundCards());
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
            } else {
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
                try {
                    gameEnded();
                } catch (IOException e) {
                    showDialog("Error", "Couldn't end game!");
                }
            }
            clicks = 0;
        }
    }

    public void gameEnded() throws IOException {
        Stage primaryStage = (Stage) back.getScene().getWindow();
        GaussianBlur blur = new GaussianBlur(3);
        game.setEffect(blur);

        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("EndDialog.fxml"));

        Stage dialog = new Stage();
        dialog.setTitle("Game Ended!");

        Scene scene = new Scene(root, 425, 200);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);

        double centerXPosition = primaryStage.getX() + primaryStage.getWidth() / 2d;
        double centerYPosition = primaryStage.getY() + primaryStage.getHeight() / 2d;

        dialog.setOnShowing(event -> dialog.hide());

        dialog.setOnShown(event -> {
            dialog.setX(centerXPosition - dialog.getWidth() / 2d);
            dialog.setY(centerYPosition - dialog.getHeight() / 2d);
            dialog.show();
        });

        dialog.show();
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

    public void backClicked() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Menu.fxml"));
        Stage stage = (Stage) back.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void createImageViews() {
        grid.setHgap(10);
        grid.setVgap(10);

        int rows = 4;
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

    public void createCards() {
        CardDao cardDao = new CardDao();
        try {
            ArrayList<Card> dbCards = cardDao.getAll();
            for (Card card : dbCards) {
                answerCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(), new Image("Images/Cards/backgroundSmall.png")));
                questionCards.add(new Card(card.getId(), card.getQuestion(), card.getAnswer(), new Image("Images/Cards/backgroundSmall.png")));
            }
        } catch (SQLException e) {
            showDialog("Error", "Couldn't load cards!");
        }
    }

    public void setImages() {
        if (answerCards.size() <= 12)
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
