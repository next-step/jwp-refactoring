package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Price;

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

	public String name() {
		return name;
	}

	public Price price() {
		return Price.from(price);
	}

	public Long menuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductRequest> menuProductRequests() {
		return menuProductRequests;
	}
}
