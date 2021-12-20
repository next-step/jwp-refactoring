package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.Value;

@Embeddable
public class ProductPrice extends Value<ProductPrice> {
	@Column(name = "price")
	private BigDecimal value;

	protected ProductPrice() {
	}

	public static ProductPrice of(BigDecimal price) {
		if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
		}

		ProductPrice productPrice = new ProductPrice();
		productPrice.value = price;
		return productPrice;
	}

	public BigDecimal getValue() {
		return value;
	}
}
