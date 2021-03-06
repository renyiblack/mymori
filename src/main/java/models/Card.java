package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Card {
    private Integer id;
    private String question;
    private String answer;

    public Card(){
        id = -1;
    }

    public static Card fromRs(ResultSet rs) throws SQLException {
        Card card = new Card();

        card.setId(rs.getInt("id"));
        card.setQuestion(rs.getString("question"));
        card.setAnswer(rs.getString("answer"));

        return card;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean equals(Card card) {
        return this.id.equals(card.getId());
    }
}
