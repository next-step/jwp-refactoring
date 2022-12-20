package kitchenpos.menu.ui.request;

import kitchenpos.common.domain.Quantity;

public class MenuProductRequest {

	private final long productId;
	private final long quantity;

	public MenuProductRequest(long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public long productId() {
		return productId;
	}

	public Quantity quantity() {
		return Quantity.from(quantity);
	}
}
