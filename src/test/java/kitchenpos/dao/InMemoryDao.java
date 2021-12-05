package kitchenpos.dao;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class InMemoryDao<E> {

    protected final Map<Long, E> db;
    protected Long id;
    protected String keyColumnName;

    public InMemoryDao(String keyColumnName) {
        this.db = new HashMap();
        this.keyColumnName = keyColumnName;
        this.id = 0L;
    }

    public InMemoryDao() {
        this("id");
    }

    public E save(E entity) {
        db.put(++id, entity);
        ReflectionTestUtils.setField(entity, keyColumnName, id);
        return entity;
    }

    public List<E> findAll() {
        return new ArrayList(db.values());
    }

    public Optional<E> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }
}
