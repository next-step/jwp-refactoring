package kitchenpos.product.port;

import kitchenpos.product.domain.Product;

import java.util.List;

public interface ProductPort {
    Product save(Product entity);

    Product findById(Long id);

    List<Product> findAll();

    List<Product> findAllByIdIn(List<Long> productId);
}
