package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductResponse> menuProducts;


	public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
	                    List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(List<MenuProduct> savedMenuProducts) {
		List<MenuProductResponse> menuProductResponse = savedMenuProducts.stream()
				.map(MenuProductResponse::of)
				.collect(Collectors.toList());

		Menu savedMenu = savedMenuProducts.get(0).getMenu();

		return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
				savedMenu.getMenuGroup().getId(), menuProductResponse);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
