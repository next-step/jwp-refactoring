package kitchenpos.order.domain;

import kitchenpos.common.entity.BaseIdEntity;
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

	@ManyToOne
	@JoinColumn(name = "order_table_id", nullable = false)
	private OrderTable orderTable;

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

	static Order createCookingOrder(OrderTable orderTable, List<OrderItem> items) {
		return new Order(orderTable, OrderStatus.COOKING, items);
	}

	private Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderItem> items) {
		this.orderTable = orderTable;
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

	public OrderTable getOrderTable() {
		return orderTable;
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
		return Objects.equals(orderTable, order.orderTable) &&
				Objects.equals(orderStatus, order.orderStatus) &&
				Objects.equals(orderedTime, order.orderedTime) &&
				Objects.equals(orderLineItems, order.orderLineItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orderTable, orderStatus, orderedTime, orderLineItems);
	}
}
