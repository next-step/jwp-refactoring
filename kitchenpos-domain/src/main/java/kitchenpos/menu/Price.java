package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {
	private BigDecimal price;

	protected Price() {
	}

	public Price(int price) {
		this.price = new BigDecimal(price);
		validate();
	}

	private void validate() {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("0 이상의 가격을 입력해주세요.");
		}
	}

	public Price multiply(long quantity) {
		return new Price(price.multiply(new BigDecimal(quantity)).intValue());
	}

	public boolean isGreaterThan(int price){
		return this.price.compareTo(new BigDecimal(price)) > 0;
	}

	public int value() {
		return price.intValue();
	}
}
