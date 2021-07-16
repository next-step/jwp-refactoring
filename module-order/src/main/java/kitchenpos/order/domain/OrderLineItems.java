package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import kitchenpos.order.exception.OrderException;

public class OrderLineItems {

	private List<OrderLineItem> orderLineItems;

	public OrderLineItems(List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		this.orderLineItems = orderLineItems;
	}

	public Stream<OrderLineItem> stream() {
		return this.orderLineItems.stream();
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (orderLineItems.isEmpty() || Objects.isNull(orderLineItems)) {
			throw new OrderException("주문 항목이 비워져 있거나, 존재하지 않으면 주문 생성할 수 없습니다.");
		}
	}
}
