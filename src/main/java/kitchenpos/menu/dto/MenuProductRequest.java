package kitchenpos.menu.dto;

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

	public MenuProduct toEntity(Product product) {
		return MenuProduct.create(product, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

}
