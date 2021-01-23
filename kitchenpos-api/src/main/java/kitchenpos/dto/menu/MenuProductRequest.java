package kitchenpos.dto.menu;

public class MenuProductRequest {
	public Long productId;
	public Long quantity;

	protected MenuProductRequest() {
	}

	public MenuProductRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
