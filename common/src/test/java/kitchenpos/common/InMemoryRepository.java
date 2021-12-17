package kitchenpos.common;

import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.*;

public class InMemoryRepository<E> {

    protected final Map<Long, E> db;
    protected Long id;
    protected String keyColumnName;

    public InMemoryRepository(String keyColumnName) {
        this.db = new HashMap();
        this.keyColumnName = keyColumnName;
        this.id = 0L;
    }

    public InMemoryRepository() {
        this("id");
    }

    public E save(E entity) {
        try {
            Field keyColumn = entity.getClass().getDeclaredField(keyColumnName);
            keyColumn.setAccessible(true);
            Object key = keyColumn.get(entity);
            return saveOrUpdate(entity, key);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return saveOrUpdate(entity, null);
    }

    private E saveOrUpdate(E entity, Object key) {
        if (!Objects.isNull(key)) {
            db.put((Long) key, entity);
            return entity;
        }
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
