package kitchenpos.order.dto;

import java.util.List;

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

	public Long getTableId() {
		return tableId;
	}

	public List<OrderLineItemAddRequest> getOrderLineItemAddRequests() {
		return orderLineItemAddRequests;
	}
}
