package kitchenpos.menu.ui.request;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

	private final long productId;
	private final long quantity;

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

	public MenuProduct toEntity() {
		return MenuProduct.of(productId, Quantity.from(quantity));
	}
}
