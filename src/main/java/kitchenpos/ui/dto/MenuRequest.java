package kitchenpos.ui.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

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

	public Menu2 toMenu(MenuGroup menuGroup, Map<Product, Integer> productsCount) {
		return new Menu2(name, price, menuGroup, productsCount);
	}

	public List<Long> getProductsId() {
		return menuProducts.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}

	public Map<Long, Integer> getProducts() {
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
