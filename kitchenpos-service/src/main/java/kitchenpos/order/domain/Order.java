package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderTableId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems = new OrderLineItems();

	public Order() {
	}

	public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
		this.orderTableId = orderTableId;
		this.orderStatus = OrderStatus.valueOf(orderStatus);
		this.orderedTime = orderedTime;
	}

	public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = OrderStatus.valueOf(orderStatus);
		this.orderedTime = orderedTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && Objects.equals(orderStatus, order.orderStatus);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	private void checkCompleted() {
		if (orderStatus.isCompletion()) {
			throw new IllegalArgumentException("주문이 이미 완료 되었습니다. id: " + id);
		}
	}

	public Long getId() {
		return id;
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

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.getOrderLineItems();
	}

	public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAllOrderLineItems(orderLineItems);
	}

	public void changeStatus(String orderStatus) {
		checkCompleted();
		this.orderStatus = OrderStatus.valueOf(orderStatus);
	}
}
