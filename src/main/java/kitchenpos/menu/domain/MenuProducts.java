package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {}

	private MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public static MenuProducts of(List<MenuProduct> menuProducts) {
		if (menuProducts == null) {
			return new MenuProducts(new ArrayList<>());
		}
		return new MenuProducts(menuProducts);
	}

	List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}

	void validatePrice(Price price, List<Product> products) {
		if (price.compareTo(sumTotalPrice(products)) > 0) {
			throw new IllegalArgumentException("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
		}
	}

	void toMenu(Menu menu) {
		for (MenuProduct menuProduct : menuProducts) {
			menuProduct.toMenu(menu);
		}
	}

	private Price sumTotalPrice(List<Product> products) {
		return products.stream()
			.map(this::calculatePrice)
			.reduce(Price::plus)
			.orElseThrow(() -> new IllegalArgumentException("메뉴에 대한 상품 가격을 구할 수 없습니다."));
	}

	private Price calculatePrice(Product product) {
		Quantity quantity = findQuantity(product);
		return product.calculatePrice(quantity);
	}

	private Quantity findQuantity(Product product) {
		return menuProducts.stream()
			.filter(menuProduct -> menuProduct.matchProductId(product.getId()))
			.map(MenuProduct::getQuantity)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("상품에 대한 수량을 구할 수 없습니다."));
	}
}