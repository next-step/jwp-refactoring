package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
@Transactional
public class ProductService {
	private final ProductDao productDao;

	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductResponse create(final ProductRequest request) {
		return ProductResponse.from(productDao.save(request.toProduct()));
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> list() {
		return ProductResponse.newList(productDao.findAll());
	}

	public Product findById(final Long id) {
		return productDao.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 Product가 존재하지 않습니다."));
	}
}
