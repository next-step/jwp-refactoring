package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {
	private static final long MIN_MENU_PRICE = 0;

	@Column(name = "price")
	BigDecimal menuPrice;

	protected MenuPrice() {
	}

	public MenuPrice(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
		if (Objects.isNull(menuPrice) || menuPrice.longValue() < MIN_MENU_PRICE) {
			throw new IllegalArgumentException("메뉴 가격은 " + MIN_MENU_PRICE + " 이상이어야 합니다.");
		}
		BigDecimal sum = menuProducts.stream()
			.map(MenuProduct::getSumPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		if (menuPrice.longValue() > sum.longValue()) {
			throw new IllegalArgumentException("메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없습니다.");
		}
		this.menuPrice = menuPrice;
	}

	public BigDecimal getValue() {
		return menuPrice;
	}
}
