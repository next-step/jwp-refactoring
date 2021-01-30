package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Price {
	public static final int PRICE_MUST_BIG_ZERO = 0;
	private static final String PRICE_CREATE_ERROR = "가격은 0원 이상이어야 합니다.";

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

	public Long priceToLong() {
		return this.price.longValue();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public boolean isBigger(Price totalPrice) {
		return this.price.compareTo(totalPrice.getPrice()) > PRICE_MUST_BIG_ZERO;
	}

	private void validatePrice(long price) {
		if (price < PRICE_MUST_BIG_ZERO) {
			throw new IllegalArgumentException(PRICE_CREATE_ERROR);
		}
	}
}
