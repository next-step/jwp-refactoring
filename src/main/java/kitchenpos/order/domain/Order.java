package kitchenpos.order.domain;

import static kitchenpos.order.exception.CannotStartOrderException.TYPE.NO_ORDER_ITEMS;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.AbstractAggregateRoot;

import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.CannotStartOrderException;

@Entity
@Table(name = "orders")
public class Order extends AbstractAggregateRoot<Order> {

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

	public Order(Long orderTableId, OrderLineItems orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderedTime = LocalDateTime.now();
		this.orderLineItems.addAll(orderLineItems);
		this.orderStatus = OrderStatus.COOKING;
	}

	public Order(Long id,
				 OrderLineItems orderLineItems,
				 Long orderTableId,
				 OrderStatus orderStatus,
				 LocalDateTime orderedTime) {
		this.id = id;
		this.orderLineItems.addAll(orderLineItems);
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

	public void place(OrderTableValidator orderTableValidator) {
		orderedTime = LocalDateTime.now();
		validate(orderTableValidator);
		changeOrderStatus(OrderStatus.COOKING);
		registerEvent(new OrderCookingEvent(this));
	}

	public void changeOrderStatus(OrderStatus toStatus) {
		if (isCompleted()) {
			throw new CannotChangeOrderStatusException();
		}
		orderStatus = toStatus;
		publishOrderCompletedEvent();
	}

	private void publishOrderCompletedEvent() {
		if (isCompleted()) {
			registerEvent(new OrderCompletedEvent(this));
		}
	}

	public boolean isCompleted() {
		return orderStatus == OrderStatus.COMPLETION;
	}

	private void validate(OrderTableValidator orderTableValidator) {
		shouldOrderLineItemNotEmpty();
		orderTableValidator.validate(this);
	}

	private void shouldOrderLineItemNotEmpty() {
		if (orderLineItems.isEmpty()) {
			throw new CannotStartOrderException(NO_ORDER_ITEMS);
		}
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
