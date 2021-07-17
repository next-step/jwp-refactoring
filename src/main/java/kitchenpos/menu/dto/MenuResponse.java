package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuResponse {
	private Long id;
	private String name;
	private Price price;
	private MenuGroup menuGroup;
	private List<MenuProduct> menuProducts;

	public MenuResponse() {
	}

	public MenuResponse(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		this.id = id;
		this.name = name;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public Long getId() {
		return id;
	}

	public static MenuResponse of(Menu menu) {
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup(), menu.getMenuProducts());
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.getPrice();
	}
}
