package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

@Service
public class ProductService {
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public Product create(final ProductRequest productRequest) {
		final BigDecimal price = productRequest.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		return productDao.save(Product.create(productRequest.getName(), price));
	}

	public List<Product> list() {
		return productDao.findAll();
	}
}
