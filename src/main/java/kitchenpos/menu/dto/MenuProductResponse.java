package kitchenpos.menu.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public static MenuProductResponse of(Long menuId, MenuProduct menuProduct) {
		if(menuProduct == null) {
			return null;
		}
		Long productId = Optional.ofNullable(menuProduct.getProduct())
			.map(Product::getId)
			.orElse(null);
		return new MenuProductResponse(menuId, productId, menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> newList(Long menuId, List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(it -> MenuProductResponse.of(menuId, it))
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
