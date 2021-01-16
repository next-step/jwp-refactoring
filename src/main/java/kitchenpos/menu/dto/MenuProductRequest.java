package kitchenpos.menu.dto;

public class MenuProductRequest {
	public long productId;
	public long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
}
