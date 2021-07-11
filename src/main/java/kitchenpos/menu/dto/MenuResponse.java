package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

public class MenuResponse {

	private Long id;

	private String name;

	private BigDecimal price;

	private Long menuGroupId;

	private List<MenuProductResponse> menuProducts;

	private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
		List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(Menu menu) {
		final List<MenuProductResponse> menuProducts = MenuProductResponse.listOf(menu.getMenuProducts());
		final Name menuName = menu.getName();
		final Price price = menu.getPrice();
		return new MenuResponse(menu.getId(), menuName.getValue(), price.getAmount(), menu.getMenuGroupId().getId(), menuProducts);
	}

	public static List<MenuResponse> listOf(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
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
}
