package kitchenpos.product.infra.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

public class InMemoryProductRepository implements ProductRepository {
	private final Map<Long, Product> products = new HashMap<>();

	@Override
	public Product save(Product products) {
		this.products.put(products.getId(), products);
		return products;
	}

	@Override
	public List<Product> findAll() {
		return new ArrayList<>(products.values());
	}

	@Override
	public Optional<Product> findById(Long id) {
		return Optional.ofNullable(products.get(id));
	}

	@Override
	public List<Product> findAllByIdIn(List<Long> ids) {
		return products.values()
			.stream()
			.filter(product -> ids.contains(product.getId()))
			.collect(Collectors.toList());
	}
}
