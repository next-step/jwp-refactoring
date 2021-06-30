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

	public List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}

	private Price sumPriceOfMenuProducts() {
		return this.menuProducts.stream()
			.map(MenuProduct::getPrice)
			.reduce(Price::plus)
			.orElseThrow(() -> new IllegalArgumentException("메뉴에 대한 상품 가격을 구할 수 없습니다."));
	}

	boolean isMoreExpensiveThan(Price price) {
		return price.compareTo(sumPriceOfMenuProducts()) > 0;
	}
}