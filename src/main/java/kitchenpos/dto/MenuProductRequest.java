package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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
