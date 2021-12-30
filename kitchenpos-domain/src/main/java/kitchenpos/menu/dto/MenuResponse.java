package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
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

	public MenuResponse(Menu menu) {
		this.id = menu.getId();
		this.name = menu.getName().toText();
		this.price = menu.getPrice().toBigDecimal();
		this.menuGroupId = menu.getMenuGroup().getId();
		this.menuProducts = menu.getMenuProducts().toList().stream()
			.map(MenuProductResponse::new).collect(Collectors.toList());
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
