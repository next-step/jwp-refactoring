package kitchenpos.product.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest request) {
		Product savedProduct = productRepository.save(request.toEntity());
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> list() {
		List<Product> productList = productRepository.findAll();
		return productList.stream()
			  .map(ProductResponse::of)
			  .collect(Collectors.toList());
	}

	public Product findById(Long productId) {
		return productRepository.findById(productId)
			  .orElseThrow(IllegalArgumentException::new);
	}

	public List<Product> findAllByIds(Set<Long> productIds) {
		List<Product> products = productRepository.findAllById(productIds);
		if (products.size() != productIds.size()) {
			throw new IllegalArgumentException("상품정보를 찾을 수 없습니다.");
		}

		return products;
	}
}
