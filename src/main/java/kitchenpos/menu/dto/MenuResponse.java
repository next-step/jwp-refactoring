package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
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

	public static MenuResponse of(Menu menu, List<MenuProduct> menuProducts) {
		List<MenuProductResponse> menuProductResponses = MenuProductResponse.ofList(menuProducts);
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPriceValue(), menu.getMenuGroupId(), menuProductResponses);
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

	public List<MenuProductResponse> getMenuProductResponses() {
		return menuProductResponses;
	}
}
