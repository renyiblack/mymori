package models;

import daos.CardDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    @FXML
    protected Button backButton;
    @FXML
    protected GridPane grid;
    public ArrayList<ImageView> imageViews;
    public ArrayList<Card> cards;
    public ImageView cardImage;
    protected Integer rows, columns;
    protected Double height, width;

    protected Controller() {
        rows = 2;
        columns = 6;
        height = 130.00;
        width = 90.00;
        imageViews = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public void startGame() {
        createImageViews();
        createCards();
        setImages();
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
    }

    public void backClicked() {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("Menu.fxml"));
            if (backButton != null && backButton.getScene() != null && backButton.getScene().getWindow() != null) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            showDialog("Error", "Couldn't go back to menu!");
        }
    }

    public void createImageViews() {
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

    public void createCards() {
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

    public static void showDialog(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showImportantDialog(Button button, AnchorPane anchorPane, String FXML, String title) throws Exception {
        Stage primaryStage = (Stage) button.getScene().getWindow();
        GaussianBlur blur = new GaussianBlur(3);
        anchorPane.setEffect(blur);

        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(FXML));

        Stage dialog = new Stage();
        dialog.setTitle(title);

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

    public void setImages() {
        if (cards.size() <= imageViews.size())
            for (int i = 0; i < cards.size(); i++) {
                imageViews.get(i).setImage(cards.get(i).getQuestion());
            }
    }
}
