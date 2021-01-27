package kitchenpos.menu.dto;

public class MenuProductRequest {
	private Long menuId;
	private Long productId;
	private Long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(Long menuId, Long productId, Long quantity) {
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
