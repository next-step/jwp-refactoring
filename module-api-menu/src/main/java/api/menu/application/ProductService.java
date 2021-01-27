package api.menu.application;

import api.menu.dto.ProductRequest;
import api.menu.dto.ProductResponse;
import common.entity.Price;
import domain.menu.Product;
import domain.menu.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductResponse create(final ProductRequest request) {
		Product product = productRepository.save(new Product(request.getName(), new Price(request.getPrice())));
		return ProductResponse.of(product);
	}

	public List<ProductResponse> list() {
		return productRepository.findAll().stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}
}
