package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
@Transactional
public class ProductService {
	private final ProductDao productDao;

	public ProductService(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Transactional
	public ProductResponse create(ProductRequest productRequest) {
		Product savedProduct = productDao.save(new Product(productRequest.getName(), productRequest.getPrice()));
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> list() {
		return productDao.findAll().stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
