package kitchenpos.product.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

public class Products {
	private final List<Product> products = new ArrayList<>();

	private Products(List<Product> products) {
		this.products.addAll(products);
	}

	public static Products of(List<Product> products) {
		return new Products(products);
	}

	public Product findId(Long productId) {
		return this.products.stream()
			.filter(it -> it.getId().equals(productId))
			.findFirst()
			.orElseThrow(EntityNotFoundException::new);
	}
}
