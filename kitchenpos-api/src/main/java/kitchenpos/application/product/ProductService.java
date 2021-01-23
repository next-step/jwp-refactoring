package kitchenpos.application.product;


import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest request) {
		Product savedProduct = productRepository.save(request.toEntity());
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> listProducts() {
		return ProductResponse.of(productRepository.findAll());
	}
}
