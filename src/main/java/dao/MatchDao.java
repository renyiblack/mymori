package dao;

import models.Card;
import repositories.DaoInterface;

import java.sql.*;

public class MatchDao implements DaoInterface<Card> {
    private Connection conn;

    @Override
    public void connect() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/?user=root";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new Error("Failed connection!", e);
        }
    }

    @Override
    public void insert(Card card) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement = conn
                    .prepareStatement("INSERT INTO cards (question, answer) VALUES (?, ?);");

            preparedStatement.setString(1, card.getQuestion());
            preparedStatement.setString(2, card.getAnswer());

            ResultSet rs = preparedStatement.executeQuery();

            card.setId(Integer.valueOf(rs.getString("id")));

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.close();
            }
            throw new Error("Couldn't insert match", e);
        }
    }

    @Override
    public Card get(int id) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from cards where cards.id = ?;");

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            Card card = Card.fromRs(rs);

            if (conn != null) {
                conn.close();
            }

            return card;
        } catch (SQLException e) {
            if (conn != null) {
                conn.close();
            }
            throw new Error("Couldn't get card", e);
        }
    }

    @Override
    public boolean update(Card card) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement =
                    conn.prepareStatement("UPDATE cards c SET c.question = ?, c.answer = ? WHERE c.id = ?;");

            preparedStatement.setString(1, card.getQuestion());
            preparedStatement.setString(2, card.getAnswer());
            preparedStatement.setInt(3, card.getId());

            int updateCount = preparedStatement.executeUpdate();

            if (conn != null) {
                conn.close();
            }

            return updateCount != 0;
        } catch (SQLException e) {
            if (conn != null) {
                conn.close();
            }

            return false;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement =
                    conn.prepareStatement("DELETE FROM cards WHERE cards.id = ?;");

            preparedStatement.setInt(1, id);

            boolean removed = preparedStatement.execute();

            if (conn != null) {
                conn.close();
            }

            return removed;
        } catch (SQLException e) {
            if (conn != null) {
                conn.close();
            }

            return false;
        }
    }
}
