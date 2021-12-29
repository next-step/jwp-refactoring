package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

	private Long seq;
	private Long menuId;
	private Long productId;
	private long quantity;

	public MenuProductResponse() {
	}

	public MenuProductResponse(long seq, long menuId, long productId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
			menuProduct.getQuantityValue());
	}

	public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::of)
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
