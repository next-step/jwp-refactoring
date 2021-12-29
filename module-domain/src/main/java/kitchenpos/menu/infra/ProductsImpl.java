package kitchenpos.menu.infra;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Products;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Component
public class ProductsImpl implements Products {
	private final ProductRepository productRepository;

	public ProductsImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public boolean containsAll(List<Long> ids) {
		List<Product> products = productRepository.findAllByIdIn(ids);
		return products.size() == ids.size();
	}

	@Override
	public kitchenpos.menu.domain.Product findById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
		return kitchenpos.menu.domain.Product.from(product.getPrice().getValue());
	}
}
