package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
	private Long id;
	private Long menuId;
	private Long productId;
	private Long quantity;

	public MenuProductResponse(Long id, Long menuId, Long productId, Long quantity) {
		this.id = id;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(
				menuProduct.getId(),
				menuProduct.getMenu().getId(),
				menuProduct.getProduct().getId(),
				menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
		return menuProducts.stream().map(MenuProductResponse::of)
				.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
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
