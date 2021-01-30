package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
	private String name;
	private Integer price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProducts;

	protected MenuRequest() {
	}

	public MenuRequest(String name, Integer price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
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

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public List<Long> getProductsIds() {
		return menuProducts.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}
}
