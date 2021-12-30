package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.domain.Order;
import kitchenpos.order.domain.domain.OrderLineItem;
import kitchenpos.ordertable.domain.domain.OrderTable;

public class OrderAddRequest {

	private Long tableId;
	private List<OrderLineItemAddRequest> orderLineItemAddRequests;

	protected OrderAddRequest(){
	}

	private OrderAddRequest(Long tableId, List<OrderLineItemAddRequest> orderLineItemAddRequests) {
		this.tableId = tableId;
		this.orderLineItemAddRequests = orderLineItemAddRequests;
	}

	public static OrderAddRequest of(Long tableId, List<OrderLineItemAddRequest> orderLineItemAddRequests) {
		return new OrderAddRequest(tableId, orderLineItemAddRequests);
	}

	public Order toEntity(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
		return Order.ofCooking(orderTable, orderLineItems);
	}

	public Long getTableId() {
		return tableId;
	}

	public List<OrderLineItemAddRequest> getOrderLineItemAddRequests() {
		return orderLineItemAddRequests;
	}
}
