package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dto.MenuGroupResponse;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroup;
	private List<MenuProductResponse> menuProducts;

	protected MenuResponse() {
	}

	private MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup,
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
			menu.getMenuProducts().stream()
				.map(MenuProductResponse::of)
				.collect(Collectors.toList())
		);
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

	public MenuGroupResponse getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
