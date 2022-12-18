package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;

public class MenuRequest {

	private final String name;
	private final BigDecimal price;
	private final Long menuGroupId;
	private final List<MenuProductRequest> menuProductRequests;

	public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
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

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}

	public Menu toEntity() {
		// todo : 구현 필요
		return null;
	}
}
