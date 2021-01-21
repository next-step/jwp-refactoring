package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Price {
	public static final int PRICE_MUST_BIG_ZERO = 0;
	private static final String PRICE_CREATE_ERROR = "금액은 0보다 커야 합니다.";

	private BigDecimal price;

	protected Price() {
	}

	protected Price(long price) {
		validatePrice(price);
		this.price = new BigDecimal(price);
	}

	public static Price of(long price) {
		return new Price(price);
	}

	private void validatePrice(long price) {
		if (price < PRICE_MUST_BIG_ZERO) {
			throw new IllegalArgumentException(PRICE_CREATE_ERROR);
		}
	}

	public Long getPrice() {
		return this.price.longValue();
	}
}
