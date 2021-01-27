package kitchenpos.web.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.product.domain.Product;
import kitchenpos.web.product.dto.ProductRequest;
import kitchenpos.web.product.dto.ProductResponse;
import kitchenpos.web.product.repository.ProductRepository;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(ProductRequest productRequest) {
		Product savedProduct = productRepository.save(new Product(productRequest.getName(), productRequest.getPrice()));
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> list() {
		return productRepository.findAll().stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
