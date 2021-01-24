package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;

public class MenuResponse {
	private final Long id;
	private final String name;
	private final Price price;
	private final Long menuGroupId;
	private final List<MenuProductResponse> menuProducts;

	private MenuResponse(final Long id, final String name, final Price price, final Long menuGroupId,
		final List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(final Long id, final String name, final Price price, final Long menuGroupId,
		final List<MenuProductResponse> menuProducts) {
		return new MenuResponse(id, name, price, menuGroupId, menuProducts);
	}

	public static MenuResponse of(final List<MenuProduct> menuProducts) {
		Menu menu = menuProducts.get(0).getMenu();

		List<MenuProductResponse> menuProductResponses = menuProducts.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());

		return of(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
			menuProductResponses);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
