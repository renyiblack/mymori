package models;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Card {
    private final int id;
    private final Image question;
    private final Image answer;
    private final Image background;
    private boolean isFlipped;
    private boolean isAnswer;

    public Card(int id, Image question, Image answer, Image background) {
        this.question = question;
        this.answer = answer;
        this.background = background;
        this.id = id;
        this.isFlipped = false;
        this.isAnswer = true;
    }

    public static Card fromRs(ResultSet rs) throws SQLException {
        InputStream questionImg;
        InputStream answerImg;
        rs.getBlob("question");
        rs.getBlob("answer");
        questionImg = rs.getBlob("question").getBinaryStream();
        answerImg = rs.getBlob("answer").getBinaryStream();

        return new Card(rs.getInt("id"), new Image(questionImg), new Image(answerImg), new Image("Images/Cards/background.png"));
    }

    public Image getQuestion() {
        return question;
    }

    public Image getAnswer() {
        return answer;
    }

    public boolean getIsAnswer() {
        return isAnswer;
    }

    public Image getBackground() {
        return background;
    }

    public int getId() {
        return id;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }
}
