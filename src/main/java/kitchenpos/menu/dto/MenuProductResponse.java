package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

	private Long productId;

	private Long quantity;

	public MenuProductResponse() {
	}

	public MenuProductResponse(MenuProduct menuProduct) {
		this.productId = menuProduct.getProduct().getId();
		this.quantity = menuProduct.getQuantity().toLong();
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
