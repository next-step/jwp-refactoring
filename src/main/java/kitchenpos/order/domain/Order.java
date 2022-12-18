package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.order.exception.CannotChangeOrderStatusException;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private OrderLineItems orderLineItems = new OrderLineItems();

	private Long orderTableId;

	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;

	private LocalDateTime orderedTime;

	protected Order() {
	}

	public Order(Long orderTableId, Map<Long, Integer> menus) {
		this.orderTableId = orderTableId;
		this.orderedTime = LocalDateTime.now();
		orderLineItems.addAll(this, menus);
	}

	public Order(Long id,
				 Map<Long, Integer> menus,
				 Long orderTableId,
				 OrderStatus orderStatus,
				 LocalDateTime orderedTime) {
		this.id = id;
		this.orderLineItems.addAll(this, menus);
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	public Long getId() {
		return id;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void place(OrderValidator orderValidator) {
		orderValidator.validate(this);

		orderStatus = OrderStatus.COOKING;
		orderedTime = LocalDateTime.now();
	}

	public void changeOrderStatus(OrderStatus toStatus) {
		if (isCompleted()) {
			throw new CannotChangeOrderStatusException();
		}
		orderStatus = toStatus;
	}

	public boolean isCompleted() {
		return orderStatus == OrderStatus.COMPLETION;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order)o;
		return id.equals(order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
