package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductDto;

@Service(value = "ToBeProductService")
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductDto create(ProductCreateRequest request) {
		Product product = productRepository.save(request.toToBeProduct());
		return ProductDto.of(product);
	}

	public List<ProductDto> list() {
		List<Product> products = productRepository.findAll();
		return products.stream()
			.map(ProductDto::of)
			.collect(Collectors.toList());
	}
}
