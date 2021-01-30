package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
	@Column(name = "quantity")
	private long quantity;

	protected MenuProductQuantity() {
	}

	public MenuProductQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getValue() {
		return quantity;
	}
}
