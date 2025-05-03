package service;

import java.util.List;

public interface IService<T> {
    void create(T t);
    void update(T t);
    void delete(T t);
    List<T> getAll();
    T getById(int id);
}
