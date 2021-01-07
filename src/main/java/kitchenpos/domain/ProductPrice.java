package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.WrongPriceException;

@Embeddable
public class ProductPrice {
	private BigDecimal price;

	public ProductPrice() {
	}

	private ProductPrice(BigDecimal price) {
		validatePrice(price);
		this.price = price;
	}

	public static ProductPrice of(BigDecimal price) {
		return new ProductPrice(price);
	}

	private void validatePrice(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongPriceException("상품의 가격이 없거나 0보다 작습니다.");
		}
	}

	public BigDecimal getPrice() {
		return price;
	}
}
