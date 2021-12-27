package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {

	private Long productId;

	private Long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public MenuProduct toEntity(Menu menu, Product product) {
		return MenuProduct.create(menu, product, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
