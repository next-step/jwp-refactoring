package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		return ProductResponse.from(productRepository.save(productRequest.toEntity()));
	}

	public List<ProductResponse> list() {
		return productRepository.findAll()
			.stream()
			.map(ProductResponse::from)
			.collect(Collectors.toList());
	}

	public Product findProductById(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorMessage.notFoundEntity("상품", id)));
	}
}
