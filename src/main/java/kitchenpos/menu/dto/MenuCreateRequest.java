package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductDto> menuProducts;

	public MenuCreateRequest() {
	}

	public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
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

	public List<MenuProductDto> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(List<MenuProductDto> menuProducts) {
		this.menuProducts = menuProducts;
	}
}
