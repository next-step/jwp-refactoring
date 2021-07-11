package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {
	private Long productId;

	private Long quantity;

	public MenuProductRequest() { }

	public MenuProductRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	boolean isEqualProductId(Product product) {
		return this.productId.equals(product.getId());
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	MenuProduct toEntity(Product product) {
		return new MenuProduct(product, quantity);
	}
}
