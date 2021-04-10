package model.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> extends AutoCloseable{

    void add(T obj) throws SQLException;
    void update(T obj);
    void delete(T obj);
    List<T> findAll();
    T findById(int id);
    T findByName(String name) throws SQLException;
}
