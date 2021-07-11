package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {
	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroup;
	private List<MenuProductResponse> menuProducts;

	protected MenuResponse() {
	}

	public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup,
		List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(Menu menu) {
		MenuGroupResponse menuGroupResponse = MenuGroupResponse.of(menu.getMenuGroup());
		List<MenuProductResponse> menuProducts = menu.getMenuProducts()
			.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());

		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getPrice(), menuGroupResponse,
			menuProducts);
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuResponse that = (MenuResponse)o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name)
			&& Objects.equals(price, that.price) && Objects.equals(menuGroup.getId(), that.menuGroup.getId())
			&& Objects.equals(menuProducts, that.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, menuGroup, menuProducts);
	}
}
