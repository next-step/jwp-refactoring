package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProducts;

	public void setMenuProducts(List<MenuProductRequest> menuProducts) {
		this.menuProducts = menuProducts;
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

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<Long> getProductIds() {
		return menuProducts.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}

	public List<Long> getQuantities() {
		return menuProducts.stream()
			.map(MenuProductRequest::getQuantity)
			.collect(Collectors.toList());
	}

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}
}
