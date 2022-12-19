package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderTableId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.COOKING;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	public Order() {
	}

	public Order(
		Long orderTableId,
		OrderLineItems orderLineItems
	) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public static Order of(long orderTableId, OrderLineItems orderLineItems) {
		return new Order(orderTableId, orderLineItems);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(final Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus.name();
	}

	public void updateStatus(final OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public void setOrderedTime(final LocalDateTime orderedTime) {
		this.orderedTime = orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.list();
	}

	public void setOrderLineItems(final OrderLineItems orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
}
