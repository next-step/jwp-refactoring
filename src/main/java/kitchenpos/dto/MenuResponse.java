package kitchenpos.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuResponse {
	private long id;
	private String name;
	private BigDecimal price;
	private long menuGroupId;
	private List<MenuProductResponse> menuProducts;

	public static MenuResponse of(Menu menu) {
		List<MenuProductResponse> menuProductResponses = new ArrayList<>();
		menu.getMenuProducts().forEachRemaining(menuProduct -> {
			final MenuProductResponse of = MenuProductResponse.of(menuProduct);
			menuProductResponses.add(of);
		});

		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(),
				menu.getMenuGroup().getId(), menuProductResponses);
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

