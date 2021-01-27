package kitchenpos.web.orders.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
public class OrderRequest {
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

	public OrderRequest() {
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
}
