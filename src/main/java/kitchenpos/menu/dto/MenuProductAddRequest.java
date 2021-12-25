package kitchenpos.menu.dto;

public class MenuProductAddRequest {

	private Long productId;

	private Long quantity;

	protected MenuProductAddRequest() {
	}

	private MenuProductAddRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductAddRequest of(Long productId, Long quantity) {
		return new MenuProductAddRequest(productId, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
