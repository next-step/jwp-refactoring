package kitchenpos.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
	Product save(Product product);

	List<Product> findAll();

	Optional<Product> findById(Long id);

	List<Product> findAllByIdIn(List<Long> ids);
}
