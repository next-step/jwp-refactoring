package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		Price price = new Price(productRequest.getPrice());
		Product product = new Product(productRequest.getName(), price);
		return ProductResponse.of(productRepository.save(product));
	}

	public List<ProductResponse> list() {
		return productRepository.findAll().stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}
}
