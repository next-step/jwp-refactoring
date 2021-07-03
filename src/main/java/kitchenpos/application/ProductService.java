package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@Service
public class ProductService {
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public Product create(final Product product) {
		final BigDecimal price = product.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("상품 가격은 0보다 작을 수 없습니다.");
		}

		return productDao.save(product);
	}

	public List<Product> list() {
		return productDao.findAll();
	}
}
