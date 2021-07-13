package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroupResponse;

	public MenuResponse(Long id, String name, BigDecimal price,
		MenuGroupResponse menuGroupResponse) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupResponse = menuGroupResponse;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public MenuGroupResponse getMenuGroupResponse() {
		return menuGroupResponse;
	}

	public static MenuResponse of(Menu menu) {
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().value(),
			MenuGroupResponse.of(menu.getMenuGroup()));
	}

	public static List<MenuResponse> of(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}
}
