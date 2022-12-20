package kitchenpos.menu.ui.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProducts;

public class MenuProductResponse {

	private final long seq;
	private final long menuId;
	private final long productId;
	private final long quantity;

	private MenuProductResponse(long seq, long menuId, long productId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(long seq, long menuId, long productId, long quantity) {
		return new MenuProductResponse(seq, menuId, productId, quantity);
	}

	public static List<MenuProductResponse> listFrom(MenuProducts menuProducts) {
		return menuProducts.list().stream()
			.map(menuProduct -> MenuProductResponse.of(menuProduct.getSeq(), menuProduct.getMenuId(),
				menuProduct.getProductId(), menuProduct.getQuantity()))
			.collect(Collectors.toList());
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

	public long getQuantity() {
		return quantity;
	}
}

