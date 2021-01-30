package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
	private Long seq;
	private Long menuId;
	private ProductResponse product;
	private long quantity;

	protected MenuProductResponse() {
	}

	public MenuProductResponse(Long seq, Long menuId, ProductResponse product, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), ProductResponse.of(menuProduct.getProduct()),
			menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
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

	public ProductResponse getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}
}
