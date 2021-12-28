package kitchenpos.dao;

import kitchenpos.domain.Product;

import java.util.*;

public class FakeProductDao implements ProductDao {
    private Map<Long, Product> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public Product save(Product product) {
        product.createId(key);
        map.put(key, product);
        key++;
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Product product = map.get(id);
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(map.values());
    }
}
