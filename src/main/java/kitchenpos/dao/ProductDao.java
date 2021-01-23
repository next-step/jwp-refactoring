package kitchenpos.dao;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface ProductDao {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
