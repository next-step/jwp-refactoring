package kitchenpos.application;

import kitchenpos.common.Price;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
		Product product = productDao.save(new Product(request.getName(), new Price(request.getPrice())));
		return ProductResponse.of(product);
	}

	public List<ProductResponse> list() {
		return productDao.findAll().stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}
}
