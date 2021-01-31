package kitchenpos.menu.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.menu.Menu;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroup;
	private List<MenuProductResponse> menuProducts;

	public static MenuResponse of(final Menu menu) {
		return Builder.MenuResponse()
			.id(menu.getId())
			.name(menu.getName())
			.price(menu.getPrice())
			.menuGroup(MenuGroupResponse.of(menu.getMenuGroup()))
			.menuProducts(MenuProductResponse.ofList(menu.getMenuProducts()))
			.build();
	}

	public MenuResponse(final Long id, final String name, final BigDecimal price, final MenuGroupResponse menuGroup,
		final List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static List<MenuResponse> ofList(final List<Menu> menus) {
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

	public MenuGroupResponse getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}

	public static final class Builder {
		private Long id;
		private String name;
		private BigDecimal price;
		private MenuGroupResponse menuGroup;
		private List<MenuProductResponse> menuProducts;

		private Builder() {
		}

		public static Builder MenuResponse() {
			return new Builder();
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder price(BigDecimal price) {
			this.price = price;
			return this;
		}

		public Builder menuGroup(MenuGroupResponse menuGroup) {
			this.menuGroup = menuGroup;
			return this;
		}

		public Builder menuProducts(List<MenuProductResponse> menuProducts) {
			this.menuProducts = menuProducts;
			return this;
		}

		public MenuResponse build() {
			return new MenuResponse(id, name, price, menuGroup, menuProducts);
		}
	}
}
