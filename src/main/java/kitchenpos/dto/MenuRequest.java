package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProducts;

	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId,
		List<MenuProductRequest> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
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

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public Menu toMenu() {
		return new Menu(this.name, this.price, this.menuGroupId, this.toMenuProducts());
	}

	private List<MenuProduct> toMenuProducts() {
		return this.menuProducts.stream()
			.map(menuProduct -> menuProduct.toMenuProduct())
			.collect(Collectors.toList());
	}
}
