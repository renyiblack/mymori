package repositories;

import java.sql.Connection;
import java.sql.SQLException;

public interface DaoInterface<T> {
    void connect();

    int insert(T data) throws SQLException;

    T get(int id) throws SQLException;

    boolean update(T data) throws SQLException;

    boolean delete(int id) throws SQLException;
}
