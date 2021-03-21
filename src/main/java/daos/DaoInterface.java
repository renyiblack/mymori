package daos;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DaoInterface<T> {
    void insert(T data) throws SQLException;

    T get(int id) throws SQLException;

    ArrayList<T> getAll() throws SQLException;

    void update(T data) throws SQLException;

    void delete(int id) throws SQLException;
}
