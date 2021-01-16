package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {
	private Long productId;
	private long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(Long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public MenuProduct toMenuProduct(Product product) {
		return new MenuProduct(product, quantity);
	}
}
