package kitchenpos.ui.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kitchenpos.domain.Product;
import kitchenpos.domain.menu.Menu;

public class MenuRequest {

	private String name;
	private Long price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProducts;

	private MenuRequest() {
	}

	public MenuRequest(String name, Long price, Long menuGroupId, List<ProductResponse> products) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = MenuProductRequest.of(products);
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

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public Menu toMenu(Map<Product, Integer> productsCount) {
		return new Menu(name, price, menuGroupId, productsCount);
	}

	public List<Long> toProductsId() {
		return menuProducts.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}

	public Map<Long, Integer> toProducts() {
		return menuProducts.stream()
			.collect(Collectors.toMap(
				MenuProductRequest::getProductId, it -> 1, Integer::sum));
	}

	public static class MenuProductRequest {

		private Long productId;
		private Integer quantity;

		private MenuProductRequest() {
		}

		public MenuProductRequest(Long productId, Integer quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static List<MenuProductRequest> of(List<ProductResponse> products) {
			return products.stream()
				.map(product -> new MenuProductRequest(product.getId(), 1))
				.collect(Collectors.toList());
		}

		public Long getProductId() {
			return productId;
		}

		public Integer getQuantity() {
			return quantity;
		}
	}

}
