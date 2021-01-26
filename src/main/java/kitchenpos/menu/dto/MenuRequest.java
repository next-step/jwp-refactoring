package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import domain.menu.Menu;
import domain.menu.MenuGroup;
import domain.menu.MenuProduct;
import domain.menu.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

	public Menu createMenu(MenuGroup menuGroup) {
		return new Menu(name, price, menuGroup);
	}

	@JsonIgnore
	public List<Long> getProductIds() {
		return menuProductRequests.stream()
				.map(MenuProductRequest::getProductId).collect(Collectors.toList());
	}

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}

	public List<MenuProduct> createMenuProducts(List<Product> products) {
		return menuProductRequests.stream()
				.map(iter -> iter.toMenuProduct(products))
				.collect(Collectors.toList());
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
}
