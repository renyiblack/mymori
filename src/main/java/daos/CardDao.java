package daos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import models.Card;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CardDao implements DaoInterface<Card> {
    private Connection conn;

    @Override
    public void connect() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/mymori?user=dev&password=dev";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new Error("Failed connection!", e);
        }
    }

    private SerialBlob imageToBlob(Image image) throws IOException, SQLException {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] byteArray = s.toByteArray();
        s.close();
        return new SerialBlob(byteArray);
    }

    @Override
    public void insert(Card card) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement = conn
                    .prepareStatement("INSERT INTO cards (question, answer) VALUES (?, ?);");

            preparedStatement.setBlob(1, imageToBlob(card.getQuestion()));
            preparedStatement.setBlob(2, imageToBlob(card.getAnswer()));

            preparedStatement.executeUpdate();

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | IOException e) {
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
            throw new SQLException("Couldn't get card", e);
        }
    }

    public ArrayList<Card> getAll() throws SQLException {
        connect();

        ArrayList<Card> cards = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from cards;");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                cards.add(Card.fromRs(rs));
            }

            if (conn != null) {
                conn.close();
            }

            return cards;
        } catch (SQLException e) {
            if (conn != null) {
                conn.close();
            }
            throw new SQLException("Couldn't get cards", e);
        }
    }

    @Override
    public boolean update(Card card) throws SQLException {
        connect();

        try {
            PreparedStatement preparedStatement =
                    conn.prepareStatement("UPDATE cards c SET c.question = ?, c.answer = ? WHERE c.id = ?;");

            preparedStatement.setBlob(1, imageToBlob(card.getQuestion()));
            preparedStatement.setBlob(2, imageToBlob(card.getAnswer()));
            preparedStatement.setInt(3, card.getId());

            int updateCount = preparedStatement.executeUpdate();

            if (conn != null) {
                conn.close();
            }

            return updateCount != 0;
        } catch (SQLException | IOException e) {
            if (conn != null) {
                conn.close();
            }
            throw new SQLException("Couldn't update card", e);
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
            throw new SQLException("Couldn't delete card", e);
        }
    }
}
