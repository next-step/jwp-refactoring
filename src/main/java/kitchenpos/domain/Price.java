package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

	@Column(name = "price")
	private BigDecimal price;

	protected Price() {
	}

	public Price(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("상품 가격은 0보다 작을 수 없습니다.");
		}

		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public int compareTo(Price price) {
		return this.price.compareTo(price.getPrice());
	}

	public void addPrice(Price price) {
		this.price = this.price.add(price.getPrice());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Price target = (Price)o;
		return Objects.equals(price, target.getPrice());
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}

	public BigDecimal multiply(long quantity) {
		return this.price.multiply(BigDecimal.valueOf(quantity));
	}
}
