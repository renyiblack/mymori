package daos;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utils.Strings;
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

            preparedStatement.execute();

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
        } catch (SQLException | IOException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new Error(Strings.ERROR_SAVE_CARD, e);
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
            throw new SQLException(Strings.ERROR_LOAD_CARDS, e);
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
            throw new SQLException(Strings.ERROR_LOAD_CARDS, e);
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

            preparedStatement.executeUpdate();

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
        } catch (SQLException | IOException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException(Strings.ERROR_SAVE_CARD, e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            PreparedStatement preparedStatement =
                    dbSingleton.getConnection().prepareStatement("DELETE FROM cards WHERE cards.id = ?;");

            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
        } catch (SQLException e) {
            if (dbSingleton.getConnection() != null) {
                dbSingleton.getConnection().close();
            }
            throw new SQLException(Strings.ERROR_DELETE_CARD, e);
        }
    }

    private SerialBlob imageToBlob(Image image) throws IOException, SQLException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        byte[] byteArray = outputStream.toByteArray();
        outputStream.close();
        return new SerialBlob(byteArray);
    }
}
