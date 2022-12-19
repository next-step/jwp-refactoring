package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductResponse {
	private Long seq;
	private Long menuId;
	private Long productId;
	private int quantity;

	private MenuProductResponse(Long seq, Long menuId, Long productId, int quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(),
			menuProduct.getMenu().getId(),
			menuProduct.getProductId(),
			menuProduct.getQuantity().value());
	}

	public static List<MenuProductResponse> ofMenuProducts(MenuProducts menuProducts) {
		return menuProducts.value()
			.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
	}

	public int getQuantity() {
		return quantity;
	}
}
