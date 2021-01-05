package kitchenpos.dto;

public class MenuProductRequest {

	private Long productId;
	private Long quantity;

	public MenuProductRequest() {
	}

	private MenuProductRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductRequest of(Long productId, Long quantity) {
		return new MenuProductRequest(productId, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
