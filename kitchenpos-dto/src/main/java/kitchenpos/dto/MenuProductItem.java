package kitchenpos.dto;

public class MenuProductItem {

	private Long productId;
	private Long quantity;

	private MenuProductItem() {
	}

	private MenuProductItem(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductItem of(Long productId, Long quantity) {
		return new MenuProductItem(productId, quantity);
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
