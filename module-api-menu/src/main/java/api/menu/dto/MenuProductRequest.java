package api.menu.dto;

import domain.menu.MenuProduct;
import domain.menu.Product;

import java.util.List;

public class MenuProductRequest {
	private long productId;
	private long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	MenuProduct toMenuProduct(List<Product> products) {
		return new MenuProduct(findProduct(products), quantity);
	}

	private Product findProduct(List<Product> products) {
		return products.stream()
				.filter(iter -> iter.getId() == productId)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("cannot find product"));
	}
}
