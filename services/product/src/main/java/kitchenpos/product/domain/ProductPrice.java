package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
	private static final long MIN_PRODUCT_PRICE = 0;
	@Column(name = "price")
	BigDecimal productPrice;

	protected ProductPrice() {
	}

	public ProductPrice(BigDecimal productPrice) {
		if (Objects.isNull(productPrice) || productPrice.longValue() < MIN_PRODUCT_PRICE) {
			throw new IllegalArgumentException("상품 가격은 " + MIN_PRODUCT_PRICE + " 이상이어야 합니다.");
		}
		this.productPrice = productPrice;
	}

	public BigDecimal getValue() {
		return productPrice;
	}
}
