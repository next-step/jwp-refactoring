package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		Product product = productRepository.save(Product.create(productRequest.getName(), productRequest.getPrice()));
		return ProductResponse.of(product);
	}

	public List<ProductResponse> list() {
		List<Product> products = productRepository.findAll();
		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
