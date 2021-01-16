package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
	private Long seq;
	private Long menuId;
	private Long productId;
	private long quantity;

	protected MenuProductResponse() {
	}

	public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq()
			, menuProduct.getMenuId()
			, menuProduct.getProductId()
			, menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
	}
}
