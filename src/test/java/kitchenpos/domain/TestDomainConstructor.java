package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TestDomainConstructor {

	public static OrderTable orderTableWithId(Long tableGroupId, int numberOfGuests, boolean empty, Long id) {
		return new OrderTable(id, new TableGroup(), numberOfGuests, empty);
	}

	public static Order order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
		return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static Order orderWithId(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems, Long id) {
		return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static OrderLineItem orderLineItem(Long orderId, Long menuId, long quantity) {
		return new OrderLineItem(orderId, menuId, quantity);
	}

	public static OrderLineItem orderLineItemWithSeq(Long orderId, Long menuId, long quantity, Long seq) {
		return new OrderLineItem(seq, orderId, menuId, quantity);
	}
}
