package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuRequest {

	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProductRequests;

	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
	}

	public Menu toMenu() {
		return new Menu(name, Price.valueOf(price), menuGroupId);
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

	public MenuProducts toMenuProducts() {
		return new MenuProducts(menuProductRequests.stream()
			.map(mp -> new MenuProduct(mp.getProductId(), Quantity.valueOf(mp.getQuantity())))
			.collect(Collectors.toList()));
	}
}
