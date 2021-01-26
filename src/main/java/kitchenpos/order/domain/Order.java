package kitchenpos.order.domain;

import common.entity.BaseIdEntity;
import kitchenpos.order.application.OrderValidationException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseIdEntity {

	static final String MSG_CANNOT_CHANGE_COMPLETION = "Cannot change orderStatus of already COMPLETION table";

	@Column(name = "order_table_id", nullable = false, insertable = false, updatable = false)
	private long orderTableId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	@CreatedDate
	@Column(name = "ordered_time", nullable = false)
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	static Order createCookingOrder(long orderTableId, List<OrderItem> items) {
		return new Order(orderTableId, OrderStatus.COOKING, items);
	}

	private Order(long orderTableId, OrderStatus orderStatus, List<OrderItem> items) {
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = OrderLineItems.of(items);
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (canChangeOrderStatus()) {
			throw new OrderValidationException(MSG_CANNOT_CHANGE_COMPLETION);
		}
		this.orderStatus = orderStatus;
	}

	private boolean canChangeOrderStatus() {
		return this.orderStatus == OrderStatus.COMPLETION;
	}

	boolean isOngoing() {
		return orderStatus.isOngoing();
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public Iterable<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Order)) return false;
		if (!super.equals(o)) return false;
		Order order = (Order) o;
		return Objects.equals(orderTableId, order.orderTableId) &&
				Objects.equals(orderStatus, order.orderStatus) &&
				Objects.equals(orderedTime, order.orderedTime) &&
				Objects.equals(orderLineItems, order.orderLineItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orderTableId, orderStatus, orderedTime, orderLineItems);
	}
}
