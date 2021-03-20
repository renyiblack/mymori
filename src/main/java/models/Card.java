package models;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Card {
    private final Image question;
    private final Image answer;
    private final Image background;
    private final int id;

    public Card(int id, Image question, Image answer, Image background) {
        this.question = question;
        this.answer = answer;
        this.background = background;
        this.id = id;
    }

    public static Card fromRs(ResultSet rs) throws SQLException, IOException {
        rs.getBlob("question");
        rs.getBlob("answer");
        InputStream questionImg;
        InputStream answerImg;
        questionImg = rs.getBlob("question").getBinaryStream();
        answerImg = rs.getBlob("answer").getBinaryStream();

        return new Card(rs.getInt("id"), new Image(questionImg), new Image(answerImg), new Image("Images/Cards/backgroundSmall.png"));
    }

    public Image getQuestion() {
        return question;
    }

    public Image getAnswer() {
        return answer;
    }

    public Image getBackground() {
        return background;
    }

    public int getId() {
        return id;
    }
}
