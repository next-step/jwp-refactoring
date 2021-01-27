package api.menu.dto;


import domain.menu.MenuProduct;

public class MenuProductResponse {

	private long productId;
	private long quantity;

	public MenuProductResponse() {
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(
				menuProduct.getProduct().getId(),
				menuProduct.getQuantity().getValue());
	}

	public MenuProductResponse(long productId, long quantity) {
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
