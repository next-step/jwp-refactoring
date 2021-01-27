package kitchenpos.menu.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {
	private Long menuId;
	private Long productId;
	private Long quantity;

	public MenuProductResponse() {
	}

	public MenuProductResponse(Long menuId, Long productId, Long quantity) {
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse from(MenuProduct menuProduct) {
		if(menuProduct == null) {
			return null;
		}
		Long menuId = Optional.ofNullable(menuProduct.getMenu())
			.map(Menu::getId)
			.orElse(null);
		Long productId = Optional.ofNullable(menuProduct.getProduct())
			.map(Product::getId)
			.orElse(null);
		return new MenuProductResponse(menuId, productId, menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> newList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::from)
			.collect(Collectors.toList());
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
