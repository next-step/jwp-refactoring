package kitchenpos.menu.ui.request;

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
}
