package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductResponse> menuProductResponses;

	public MenuResponse() {
	}

	public MenuResponse(long id, String name, BigDecimal price, long menuGroupId,
		List<MenuProductResponse> menuProductResponses) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductResponses = menuProductResponses;
	}

	public static MenuResponse of(Menu menu) {
		List<MenuProductResponse> menuProductResponses = MenuProductResponse.ofList(menu.getMenuProducts());
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductResponses);
	}

	public static List<MenuResponse> ofList(List<Menu> Menus) {
		return Menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
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

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProductResponses() {
		return menuProductResponses;
	}
}
