package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;

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

	public static List<MenuResponse> of(List<Menu> menus) {
		return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
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

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(List<MenuProductResponse> menuProducts) {
		this.menuProducts = menuProducts;
	}
}
