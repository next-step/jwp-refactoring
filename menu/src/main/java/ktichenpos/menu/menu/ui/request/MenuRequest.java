package ktichenpos.menu.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuRequest {

	private final String name;
	private final BigDecimal price;
	private final Long menuGroupId;
	private final List<MenuProductRequest> menuProducts;


	@JsonCreator
	public MenuRequest(
		@JsonProperty("name") String name,
		@JsonProperty("price") BigDecimal price,
		@JsonProperty("menuGroupId") long menuGroupId,
		@JsonProperty("menuProducts") List<MenuProductRequest> menuProducts) {
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
}
