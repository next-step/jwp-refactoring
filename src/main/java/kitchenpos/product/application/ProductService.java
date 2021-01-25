package kitchenpos.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
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

		return ProductResponse.from(productDao.save(request.toProduct()));
	}

	public List<ProductResponse> list() {
		return ProductResponse.newList(productDao.findAll());
	}
}
