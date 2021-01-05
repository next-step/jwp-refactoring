package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

@Service
public class ProductService {
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		final BigDecimal price = productRequest.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		Product product = productDao.save(Product.create(productRequest.getName(), price));
		return ProductResponse.of(product);
	}

	public List<ProductResponse> list() {
		List<Product> products = productDao.findAll();
		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
