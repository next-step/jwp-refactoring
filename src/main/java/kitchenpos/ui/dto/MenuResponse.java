package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct2;

public class MenuResponse {

	private Long id;
	private String name;
	private Long price;
	private String menuGroup;
	private List<MenuProductResponse> menuProducts;

	public MenuResponse(Long id, String name, Long price, String menuGroup, List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static List<MenuResponse> of(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	public static MenuResponse of(Menu menu) {
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().longValue(),
								menu.getMenuGroup().getName(),
								MenuProductResponse.of(menu.getMenuProducts()));
	}

	static class MenuProductResponse {
		private Long productId;
		private Long quantity;

		private MenuProductResponse() {
		}

		public MenuProductResponse(Long productId, Long quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static List<MenuProductResponse> of(List<MenuProduct2> menuProducts) {
			return menuProducts.stream()
				.map(menuProduct -> new MenuProductResponse(menuProduct.getProductId(),
															menuProduct.getQuantity()))
				.collect(Collectors.toList());
		}

		public Long getProductId() {
			return productId;
		}

		public Long getQuantity() {
			return quantity;
		}
	}

	private MenuResponse() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}

	public String getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
