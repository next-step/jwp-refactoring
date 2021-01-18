package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public ProductResponse create(final ProductRequest request) {
		final BigDecimal price = request.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		Product product = productDao.save(new Product(request.getName(), request.getPrice()));
		return ProductResponse.of(product);
	}

	public List<ProductResponse> list() {
		return productDao.findAll().stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}
}
