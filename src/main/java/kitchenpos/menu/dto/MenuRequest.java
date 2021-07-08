package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;

public class MenuRequest {

	private String name;
	private int price;
	private Long menuGroupId;
	private List<Long> menuProductIds;

	public MenuRequest(String name, int price, Long menuGroupId, List<Long> menuProductIds) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductIds = menuProductIds;
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

	public List<Long> getMenuProductIds() {
		return menuProductIds;
	}

	public Menu toMenu(MenuGroup menuGroup, MenuProducts menuProducts) {
		return new Menu(name, new Price(BigDecimal.valueOf(price)), menuGroup, menuProducts);
	}
}
