package kitchenpos.dao;

import java.util.*;

public class InMemoryDao<E> {

    protected final Map<Long, E> db = new HashMap();
    protected Long id = 0L;

    public E save(E entity) {
        db.put(id++, entity);
        return entity;
    }

    public List<E> findAll() {
        return new ArrayList(db.values());
    }

    public Optional<E> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }
}
