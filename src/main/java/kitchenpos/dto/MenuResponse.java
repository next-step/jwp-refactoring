package kitchenpos.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
	private long id;
	private String name;
	private BigDecimal price;
	private long menuGroupId;
	private List<MenuProductResponse> menuProducts;

	public static MenuResponse of(Menu menu) {
		List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
				.map(MenuProductResponse::of)
				.collect(Collectors.toList());
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), menuProductResponses);
	}

	public MenuResponse() {
	}

	public MenuResponse(long id, String name, BigDecimal price, long menuGroupId, List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}

