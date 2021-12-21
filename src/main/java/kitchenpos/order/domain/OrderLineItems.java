package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id", nullable = false)
	private List<OrderLineItem> values = new ArrayList<>();

	protected OrderLineItems() {
	}

	public static OrderLineItems of(List<OrderLineItem> values) {
		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException("주문 항목들은 한개 이상이어야 합니다.");
		}

		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.values = values;
		return orderLineItems;
	}

	public List<OrderLineItem> getValues() {
		return values;
	}
}
