package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {}

	MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}

	boolean isMoreExpensiveThan(Price price) {
		return price.compareTo(sumPriceOfMenuProducts()) > 0;
	}

	void toMenu(Menu menu) {
		for (MenuProduct menuProduct : menuProducts) {
			menuProduct.toMenu(menu);
		}
	}

	private Price sumPriceOfMenuProducts() {
		return this.menuProducts.stream()
			.map(MenuProduct::calculatePrice)
			.reduce(Price::plus)
			.orElseThrow(() -> new IllegalArgumentException("메뉴에 대한 상품 가격을 구할 수 없습니다."));
	}
}