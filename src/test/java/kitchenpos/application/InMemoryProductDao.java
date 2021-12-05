package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductDao implements ProductDao {

    private final Map<Long, Product> db = new HashMap();
    private Long id = 0L;

    @Override
    public Product save(Product entity) {
        db.put(id++, entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
