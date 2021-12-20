package kitchenpos.product.domain;

import java.util.List;

public interface ProductRepository {
	Product save(Product product);

	List<Product> findAll();
}
