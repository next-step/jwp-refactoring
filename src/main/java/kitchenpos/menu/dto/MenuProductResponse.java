package kitchenpos.menu.dto;


import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

	private long menuId;
	private long productId;
	private long quantity;

	public MenuProductResponse() {
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getMenu().getId(),
				menuProduct.getProduct().getId(),
				menuProduct.getQuantity().getValue());
	}

	public MenuProductResponse(long menuId, long productId, long quantity) {
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}
