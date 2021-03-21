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
    private final DBSingleton dbSingleton = DBSingleton.getInstance();

    @Override
    public void insert(Card card) throws SQLException {
        try {
            PreparedStatement preparedStatement = dbSingleton.getConnection()
                    .prepareStatement("INSERT INTO cards (question, answer) VALUES (?, ?);");

            preparedStatement.setBlob(1, imageToBlob(card.getQuestion()));
            preparedStatement.setBlob(2, imageToBlob(card.getAnswer()));

            preparedStatement.executeUpdate();

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
        } catch (SQLException | IOException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new Error("Couldn't insert match", e);
        }
    }

    @Override
    public Card get(int id) throws SQLException {
        try {
            PreparedStatement preparedStatement = dbSingleton.getConnection().prepareStatement(
                    "select * from cards where cards.id = ?;");

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            Card card = Card.fromRs(rs);

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }

            return card;
        } catch (SQLException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException("Couldn't get card", e);
        }
    }

    @Override
    public ArrayList<Card> getAll() throws SQLException {
        ArrayList<Card> cards = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = dbSingleton.getConnection().prepareStatement(
                    "select * from cards;");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                cards.add(Card.fromRs(rs));
            }

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }

            return cards;
        } catch (SQLException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException("Couldn't get cards", e);
        }
    }

    @Override
    public void update(Card card) throws SQLException {
        try {
            PreparedStatement preparedStatement =
                    dbSingleton.getConnection().prepareStatement("UPDATE cards c SET c.question = ?, c.answer = ? WHERE c.id = ?;");

            preparedStatement.setBlob(1, imageToBlob(card.getQuestion()));
            preparedStatement.setBlob(2, imageToBlob(card.getAnswer()));
            preparedStatement.setInt(3, card.getId());

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
        } catch (SQLException | IOException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException("Couldn't update card", e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            PreparedStatement preparedStatement =
                    dbSingleton.getConnection().prepareStatement("DELETE FROM cards WHERE cards.id = ?;");

            preparedStatement.setInt(1, id);

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }

        } catch (SQLException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException("Couldn't delete card", e);
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
}
