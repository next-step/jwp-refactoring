package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

	private long id;
	private String name;
	private BigDecimal price;
	private long menuGroupId;
	private List<MenuProductResponse> menuProductResponses;

	public MenuResponse() {
	}

	public MenuResponse(long id, String name, BigDecimal price, long menuGroupId,
		List<MenuProductResponse> menuProductResponses) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductResponses = menuProductResponses;
	}

	public static MenuResponse of(Menu menu) {
		List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductResponses);
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

	public List<MenuProductResponse> getMenuProductResponses() {
		return menuProductResponses;
	}
}
