package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public Product create(final Product product) {
		final BigDecimal price = product.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		return productRepository.save(product);
	}

	public List<Product> list() {
		return productRepository.findAll();
	}

	public Product findById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다. id: " + id));
	}
}
