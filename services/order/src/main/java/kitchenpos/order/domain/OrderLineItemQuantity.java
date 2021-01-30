package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
	@Column(name = "quantity")
	private long quantity;

	protected OrderLineItemQuantity() {
	}

	public OrderLineItemQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getValue() {
		return quantity;
	}
}
