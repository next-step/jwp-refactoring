package kitchenpos.menu.ui.response;

public class MenuProductResponse {

	private final long seq;
	private final long menuId;
	private final long productId;
	private final int quantity;

	private MenuProductResponse(long seq, long menuId, long productId, int quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(long seq, long menuId, long productId, int quantity) {
		return new MenuProductResponse(seq, menuId, productId, quantity);
	}

	public long getSeq() {
		return seq;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}
}

