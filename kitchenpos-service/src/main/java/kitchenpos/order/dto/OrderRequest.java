package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
	private Long orderTableId;
	private LocalDateTime orderedTime;
	@NotEmpty
	private List<OrderLineItemRequest> orderLineItemRequests;

	protected OrderRequest() {
	}

	public OrderRequest(Long orderTableId, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItemIds) {
		this.orderTableId = orderTableId;
		this.orderedTime = orderedTime;
		this.orderLineItemRequests = orderLineItemIds;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderRequest that = (OrderRequest) o;
		return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderedTime, that.orderedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderTableId, orderedTime);
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemRequest> getOrderLineItemRequests() {
		return orderLineItemRequests;
	}

	public int getOrderLineItemRequestSize() {
		return orderLineItemRequests.size();
	}

	public List<Long> getMenuIds() {
		return orderLineItemRequests.stream().map(OrderLineItemRequest::getMenuId)
				.collect(Collectors.toList());
	}

	public Order toOrder() {
		return new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());
	}
}
