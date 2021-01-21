package kitchenpos.product.domain;

import java.util.List;

public class Products {
	private final List<Product> products;

	private Products(List<Product> products) {
		this.products = products;
	}

	public static Products of(List<Product> products) {
		return new Products(products);
	}
}
