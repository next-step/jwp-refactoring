package kitchenpos.order.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import kitchenpos.order.domain.order.Order;
import kitchenpos.order.dto.ordertable.OrderTableResponse;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTable;
	private String orderStatus;

	@JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
	private LocalDateTime orderedTime;

	private List<OrderLineItemResponse> orderLineItemResponses;

	public OrderResponse(final Long id, final OrderTableResponse orderTable, final String orderStatus,
		final LocalDateTime orderedTime,
		final List<OrderLineItemResponse> orderLineItemResponses) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItemResponses = orderLineItemResponses;
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItemResponses() {
		return orderLineItemResponses;
	}

	public static List<OrderResponse> ofList(final List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	public static OrderResponse of(final Order order) {
		return Builder.OrderResponse()
			.id(order.getId())
			.orderTable(OrderTableResponse.of(order.getOrderTable()))
			.orderStatus(order.getOrderStatusName())
			.orderedTime(order.getOrderedTime())
			.orderLineItemResponses(OrderLineItemResponse.ofList(order.getOrderLineItems()))
			.build();
	}

	public static final class Builder {
		private Long id;
		private OrderTableResponse orderTable;
		private String orderStatus;
		private LocalDateTime orderedTime;
		private List<OrderLineItemResponse> orderLineItemResponses;

		private Builder() {
		}

		public static Builder OrderResponse() {
			return new Builder();
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder orderTable(OrderTableResponse orderTable) {
			this.orderTable = orderTable;
			return this;
		}

		public Builder orderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
			return this;
		}

		public Builder orderedTime(LocalDateTime orderedTime) {
			this.orderedTime = orderedTime;
			return this;
		}

		public Builder orderLineItemResponses(List<OrderLineItemResponse> orderLineItemResponses) {
			this.orderLineItemResponses = orderLineItemResponses;
			return this;
		}

		public OrderResponse build() {
			return new OrderResponse(id, orderTable, orderStatus, orderedTime, orderLineItemResponses);
		}
	}
}
