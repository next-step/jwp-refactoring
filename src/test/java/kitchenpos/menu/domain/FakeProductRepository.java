package kitchenpos.menu.domain;

import java.util.*;
import java.util.stream.Collectors;

public class FakeProductRepository implements ProductRepository {
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

    @Override
    public List<Product> findAllByIdIn(List<Long> ids) {
        return map.values().stream()
                .filter(product -> ids.contains(product.getId()))
                .collect(Collectors.toList());
    }
}
