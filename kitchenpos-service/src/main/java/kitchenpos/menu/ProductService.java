package kitchenpos.menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ValidationUtils;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest productRequest) {
		return ProductResponse.of(productRepository.save(productRequest.toProduct()));
	}

	public List<ProductResponse> list() {
		return ProductResponse.of(productRepository.findAll());
	}

	public Map<Long, Product> findAllProductByIds(List<Long> ids) {
		List<Product> products = productRepository.findAllById(ids);

		ValidationUtils.validateListSize(products, ids, "존재하지 않는 상품이 있습니다.");

		return products.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));
	}
}
