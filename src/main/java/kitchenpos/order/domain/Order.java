package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderException;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long orderTableId;

	@Enumerated(EnumType.STRING)
	@Column
	private OrderStatus orderStatus;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems = OrderLineItems.EMPTY_ITEMS;

	protected Order() {
	}

	private Order(Long orderTableId, OrderStatus orderStatus) {
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
	}

	private Order(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static Order of(Long orderTableId, OrderStatus orderStatus) {
		return new Order(orderTableId, orderStatus);
	}

	public static Order of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		return new Order(orderTableId, orderStatus, OrderLineItems.of(orderLineItems));
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		isCompletion();
		this.orderStatus = orderStatus;
	}

	public void isCompletion() {
		if (validateCompletion()) {
			throw new OrderException(ErrorCode.ORDER_IS_COMPLETION);
		}
	}

	public void isNotCompletion() {
		if (!validateCompletion()) {
			throw new OrderException(ErrorCode.ORDER_IS_NOT_COMPLETION);
		}
	}

	public boolean validateCompletion() {
		return orderStatus.equals(OrderStatus.COMPLETION);
	}

	public void addOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
		orderLineItems = OrderLineItems.of(savedOrderLineItems);
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus.name();
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.getOrderLineItems();
	}

	public OrderResponse toResDto() {
		return OrderResponse.of(id, orderTableId, orderStatus,
			OrderLineItemResponse.ofList(orderLineItems.getOrderLineItems()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order)o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
