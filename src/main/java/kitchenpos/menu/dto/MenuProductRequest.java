package kitchenpos.menu.dto;

public class MenuProductRequest {
	private Long productId;
	private int quantity;

	public MenuProductRequest() {
	}

	private MenuProductRequest(final Long productId, final int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductRequest of(final Long productId, final int quantity) {
		return new MenuProductRequest(productId, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}
}
