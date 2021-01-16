package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductResponse> menuProducts;

	public MenuResponse() {
	}

	public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
		  List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(Menu menu) {
		List<MenuProductResponse> menuProductResponse = menu.getMenuProducts().stream()
			  .map(MenuProductResponse::of)
			  .collect(Collectors.toList());

		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
			  menu.getMenuGroup().getId(), menuProductResponse);
	}

	public Long getId() {
		return id;
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

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MenuResponse that = (MenuResponse) o;
		return Objects.equals(id, that.id) && Objects
			  .equals(menuProducts, that.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, menuProducts);
	}
}
