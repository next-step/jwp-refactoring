package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.domain.Product;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		Product savedProduct = productRepository.save(productRequest.toProduct());
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> list() {
		return productRepository.findAll().stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
