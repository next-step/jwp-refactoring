package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;

public class MenuRequest {

	private String name;
	private int price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProductRequests;

	public MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}

	public Menu toMenu(MenuGroup menuGroup, MenuProducts menuProducts) {
		return new Menu(name, new Price(BigDecimal.valueOf(price)), menuGroup, menuProducts);
	}
}
