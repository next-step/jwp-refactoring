package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

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

	public static MenuResponse of(Long id, String name, Price price, MenuGroupResponse menuGroup,
		List<MenuProductResponse> menuProducts) {
		return new MenuResponse(id, name, price.getPrice(), menuGroup, menuProducts);
	}

	public static MenuResponse of(Long id, String name, Price price, MenuGroup menuGroup,
		List<MenuProduct> menuProducts) {
		return new MenuResponse(id, name, price.getPrice(), menuGroup.toResDto(),
			MenuProductResponse.ofList(menuProducts));
	}

	public static List<MenuResponse> ofList(List<Menu> menus) {
		return menus.stream()
			.map(Menu::toResDto)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public MenuGroupResponse getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
