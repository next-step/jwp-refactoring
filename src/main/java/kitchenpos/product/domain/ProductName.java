package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.domain.Value;

@Embeddable
public class ProductName extends Value<ProductName> {
	@Column(name = "name", nullable = false)
	private String value;

	protected ProductName() {
	}

	public static ProductName of(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("이름은 빈 값일 수 없습니다.");
		}

		ProductName productName = new ProductName();
		productName.value = name;
		return productName;
	}

	public String getValue() {
		return value;
	}
}
