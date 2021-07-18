package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuRequest {
	private String name;

	@NotNull
	@Min(0)
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProductRequests;

	protected MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MenuRequest that = (MenuRequest) o;
		return Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProductRequests, that.menuProductRequests);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price, menuGroupId, menuProductRequests);
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

	public Menu toMenu() {
		return new Menu(this.name, this.price, this.menuGroupId);
	}
}
