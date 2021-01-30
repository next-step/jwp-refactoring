package kitchenpos.menu.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuResponse {
	private Long id;
	private String name;
	private Long price;
	private Long menuGroupId;
	private List<MenuProductResponse> menuProducts;

	public MenuResponse() {
	}

	public MenuResponse(long id, String name, Long price, long menuGroupId,
		List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse from(Menu menu) {
		if(menu == null) {
			return null;
		}
		Long menuGroupId = Optional.ofNullable(menu.getMenuGroup())
			.map(MenuGroup::getId)
			.orElse(null);
		return new MenuResponse(
			menu.getId(),
			menu.getName(),
			menu.getMenuPrice().longValue(),
			menuGroupId,
			MenuProductResponse.newList(menu.getId(), menu.getMenuProducts())
		);
	}

	public static List<MenuResponse> newList(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::from)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
