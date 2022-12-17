package kitchenpos.port;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPort {
    Product save(Product entity);

    Product findById(Long id);

    List<Product> findAll();

    List<Product> findAllByIdIn(List<Long> productId);
}
