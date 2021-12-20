package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItem> orderLineItems;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Order() {
	}

	public Order(String orderStatus) {
		this.orderStatus=orderStatus;
	}

	public Order(Long orderTableId, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
		this(null, orderTableId, OrderStatus.COOKING.name(), orderedTime, orderLineItems);
	}

	public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(final Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(final String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public void setOrderedTime(final LocalDateTime orderedTime) {
		this.orderedTime = orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Order)) {
			return false;
		}

		Order order = (Order)o;
		return Objects.equals(getId(), order.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
