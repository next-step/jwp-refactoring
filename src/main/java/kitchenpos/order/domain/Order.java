package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_to_order_table"))
	private OrderTable orderTable;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	@Embedded
	private OrderLineItems orderLineItems = new OrderLineItems();

	protected Order() {
	}

	private Order(OrderTable orderTable, OrderLineItems orderLineItems) {
		this.orderTable = orderTable;
		this.orderedTime = LocalDateTime.now();
		this.orderLineItems = orderLineItems;
		this.orderStatus = OrderStatus.COOKING;
	}

	public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
		validateOrderTableEmpty(orderTable);
		Order order = new Order(orderTable, orderLineItems);
		order.orderLineItems.updateOrder(order);

		return order;
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		validateCurrentOrderStatus();
		this.orderStatus = orderStatus;
	}

	public boolean isFinished() {
		return orderStatus.equals(OrderStatus.COMPLETION);
	}

	public Long getId() {
		return id;
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

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
	}

	private static void validateOrderTableEmpty(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_ORDER_WHEN_TABLE_IS_EMPTY);
		}
	}

	private void validateCurrentOrderStatus() {
		if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_ORDER_STATUS_WHEN_COMPLETED);
		}
	}

}
