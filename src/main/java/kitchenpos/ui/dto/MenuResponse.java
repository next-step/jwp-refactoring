package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;

public class MenuResponse {

	private Long id;
	private String name;
	private Long price;
	private Long menuGroupId;
	private List<MenuProductResponse> menuProducts;

	public MenuResponse(Long id, String name, Long price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public MenuResponse(Menu menu) {
		this(menu.getId(), menu.getName().value(), menu.getPrice().longValue(),
			 menu.getMenuGroupId(),
			 MenuProductResponse.of(menu.getMenuProducts()));
	}

	public static List<MenuResponse> of(List<Menu> menus) {
		return menus.stream()
			.map(MenuResponse::new)
			.collect(Collectors.toList());
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

		public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
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

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
