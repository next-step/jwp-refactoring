package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

	private Long seq;

	private Long menuId;

	private Long productId;

	private Long quantity;

	public MenuProductResponse(Long seq, Long menuId, Long productId, Long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getId(),
			menuProduct.getMenu().getId(),
			menuProduct.getProductId(),
			menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
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

	public Long getQuantity() {
		return quantity;
	}
}
