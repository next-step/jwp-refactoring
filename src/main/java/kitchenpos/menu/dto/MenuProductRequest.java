package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {

	private Long productId;
	private Long menuId;
	private long quantity;

	public MenuProductRequest(Long productId, Long menuId, long quantity) {
		this.productId = productId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public MenuProduct toMenuProduct(Product product) {
		return new MenuProduct(product, new Quantity(quantity));
	}
}
