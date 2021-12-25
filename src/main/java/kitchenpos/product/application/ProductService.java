package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductAddRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductAddRequest request) {
		return ProductResponse.of(productRepository.save(request.toEntity()));
	}

	public List<ProductResponse> list() {
		return productRepository.findAll().stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
