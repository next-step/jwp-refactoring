package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.exception.WrongPriceException;

@Embeddable
public class MenuProducts {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public void add(Menu menu, BigDecimal price, List<Product> products, List<Long> quantities) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < products.size(); i++) {
			this.menuProducts.add(MenuProduct.create(menu, products.get(i), quantities.get(i)));
			sum = sum.add(products.get(i)
				.getPrice()
				.multiply(BigDecimal.valueOf(quantities.get(i))));
		}
		validatePrice(price, sum);
	}

	private void validatePrice(BigDecimal price, BigDecimal sum) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongPriceException("메뉴의 가격이 없거나 0보다 작습니다.");
		}
		if (price.compareTo(sum) > 0) {
			throw new WrongPriceException("메뉴의 가격이 상품가격의 총합보다 클 수 없습니다.");
		}
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

}
