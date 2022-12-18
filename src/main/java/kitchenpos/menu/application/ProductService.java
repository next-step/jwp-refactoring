package kitchenpos.menu.application;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public Product create(Product product) {
		return productRepository.save(product);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findAllById(List<Long> productsId) {
		List<Product> products = productRepository.findAllById(productsId);
		validateIfExists(products, productsId);
		return products;
	}

	private void validateIfExists(List<Product> products, List<Long> productsId) {
		if (products.size() != new HashSet<>(productsId).size()) {
			throw new EntityNotFoundException(Product.class, productsId);
		}
	}
}
