package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuRequest {

	private String name;

	private BigDecimal price;

	private Long menuGroupId;

	private List<MenuProductRequest> menuProducts;

	public MenuRequest() { }

	public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
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

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public List<Long> getProductIds() {
		return menuProducts.stream()
			.map(MenuProductRequest::getProductId)
			.collect(Collectors.toList());
	}

	public List<MenuProduct> toMenuProducts(List<Product> products) {
		return products.stream()
			.map(this::toMenuProduct)
			.collect(Collectors.toList());
	}

	private MenuProduct toMenuProduct(Product product) {
		return menuProducts.stream()
			.filter(menuProductRequest -> menuProductRequest.isEqualProductId(product))
			.map(menuProductRequest -> menuProductRequest.toEntity(product))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
	}
}
