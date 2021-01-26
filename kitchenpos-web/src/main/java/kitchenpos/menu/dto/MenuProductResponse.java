package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

/**
 * @author : byungkyu
 * @date : 2021/01/26
 * @description :
 **/
public class MenuProductResponse {
	private Long id;
	private Long menuId;
	private long quantity;

	public MenuProductResponse() {
	}

	public MenuProductResponse(Long id, Long menuId, long quantity) {
		this.id = id;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct){
		return new MenuProductResponse(menuProduct.getId(), menuProduct.getMenu().getId(), menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}
