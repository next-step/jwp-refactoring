package kitchenpos.product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllByIds(List<Long> productIds);

    Product save(Product product);

    List<Product> findAll();
}
