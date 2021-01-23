package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
	private String name;
	private BigDecimal price;
	private long menuGroupId;
	private List<MenuProductRequest> menuProductRequests;

	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
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

	public long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}
}
