package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuRequest {

	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProductRequests;

	public MenuRequest() {
	}

	public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
		final List<MenuProductRequest> menuProductRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductRequests = menuProductRequests;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(final Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}

	public void setMenuProductRequests(final List<MenuProductRequest> menuProductRequests) {
		this.menuProductRequests = menuProductRequests;
	}

	public Menu toMenu(final MenuGroup menuGroup, final List<Product> products) {
		Menu menu = new Menu(this.name, this.price, menuGroup);
		List<MenuProduct> menuProducts = toMenuProducts(menu, products);
		menu.addAllMenuProduct(menuProducts);
		return menu;
	}

	private List<MenuProduct> toMenuProducts(final Menu menu, final List<Product> products) {
		Map<Long, Product> productById = products.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		validateProductsRequestExists(productById);

		return this.menuProductRequests.stream()
			.filter(menuProduct -> productById.containsKey(menuProduct.getProductId()))
			.map(menuProduct -> {
				Product product = productById.get(menuProduct.getProductId());
				return new MenuProduct(menu, product, menuProduct.getQuantity());
			})
			.collect(Collectors.toList());
	}

	private void validateProductsRequestExists(final Map<Long, Product> productById) {
		this.menuProductRequests.stream()
			.filter(menuProductRequest -> !productById.containsKey(menuProductRequest.getProductId()))
			.findAny()
			.ifPresent(menuProductRequest -> {
				throw new IllegalArgumentException(String.format("요청한 %d에 대한 상품 정보가 없습니다.",
					menuProductRequest.getProductId()));
			});
	}

	public List<Long> getProductIds() {
		return this.menuProductRequests.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}
}
