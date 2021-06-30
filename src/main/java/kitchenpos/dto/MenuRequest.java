package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
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

	public Menu toEntityWith(MenuGroup menuGroup, List<Product> products) {
		List<MenuProduct> menuProductEntities = products.stream()
			.map(this::findMenuProductOf)
			.collect(Collectors.toList());

		return new Menu(name, Price.wonOf(price), menuGroup, menuProductEntities);
	}

	private MenuProduct findMenuProductOf(Product product) {
		return menuProducts.stream()
			.filter(menuProductRequest -> menuProductRequest.isEqualProductId(product))
			.map(menuProductRequest -> menuProductRequest.toEntity(product))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
	}
}
