package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuRequest {
	public String name;
	public BigDecimal price;
	public Long menuGroupId;
	public List<MenuProductRequest> menuProducts;


	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Menu toEntity(MenuGroup menuGroup, List<Product> products) {
		return new Menu(this.name, this.price, menuGroup, toMenuGroup(products));
	}

	private MenuProducts toMenuGroup(List<Product> products) {
		Map<Long, Product> productInfo = products.stream()
				.collect(Collectors.toMap(Product::getId, Function.identity()));

		return new MenuProducts(menuProducts.stream()
				.filter(menuProduct -> productInfo.containsKey(menuProduct.getProductId()))
				.map(menuProduct -> {
					Product product = productInfo.get(menuProduct.getProductId());
					return new MenuProduct(product, menuProduct.getQuantity());
				})
				.collect(Collectors.toList()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(List<MenuProductRequest> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public List<Long> getProductIds() {
		return this.menuProducts.stream().map(MenuProductRequest::getProductId).collect(Collectors.toList());
	}

}
