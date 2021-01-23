package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
	public String name;
	public BigDecimal price;
	public Long menuGroupId;
	public List<MenuProductRequest> menuProductRequests;


	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
