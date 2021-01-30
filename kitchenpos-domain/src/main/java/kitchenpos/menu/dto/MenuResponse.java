package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.Menu;

public class MenuResponse {
	private Long id;
	private String name;
	private long price;
	private MenuGroupResponse menuGroup;
	private List<MenuProductResponse> menuProducts;

	protected MenuResponse() {
	}

	public MenuResponse(Long id, String name, long price, MenuGroupResponse menuGroup,
		List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(Menu menu) {
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
			MenuGroupResponse.of(menu.getMenuGroup()),
			MenuProductResponse.of(menu.getMenuProducts()));
	}

	public static List<MenuResponse> of(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}

	public MenuGroupResponse getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
