package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.WrongPriceException;

@Embeddable
public class MenuPrice {
	private BigDecimal price;

	public MenuPrice() {
	}

	private MenuPrice(BigDecimal price) {
		validatePrice(price);
		this.price = price;
	}

	public static MenuPrice of(BigDecimal price) {
		return new MenuPrice(price);
	}

	private void validatePrice(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongPriceException("메뉴의 가격이 없거나 0보다 작습니다.");
		}
	}

	public boolean isGreaterThanProductTotal(BigDecimal totalPrice) {
		return price.compareTo(totalPrice) > 0;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
