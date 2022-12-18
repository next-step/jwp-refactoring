package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.menu.domain.Product;

public interface ProductDao {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
