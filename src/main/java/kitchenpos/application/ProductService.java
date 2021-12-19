package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
	private final static String ERROR_MESSAGE_PRICE_VALUE="상품의 가격은 0원 이상이어야 합니다.";
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public Product create(final Product product) {
		final BigDecimal price = product.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(ERROR_MESSAGE_PRICE_VALUE);
		}
		return productDao.save(product);
	}

	public List<Product> list() {
		return productDao.findAll();
	}
}
