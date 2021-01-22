package kitchenpos.order.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.order.application.OrderValidationException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseIdEntity {

	static final String MSG_CANNOT_CREATE_EMPTY_ITEMS = "Cannot create Order By empty OrderItems";
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

	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderLineItem> orderLineItems;

	protected Order() {
	}

	static Order createCookingOrder(OrderTable orderTable, List<OrderItem> items) {
		return new Order(orderTable, OrderStatus.COOKING, items);
	}

	private Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderItem> items) {
		List<OrderLineItem> orderLineItems = getOrderLineItems(items);
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = new ArrayList<>(orderLineItems);
	}

	private List<OrderLineItem> getOrderLineItems(List<OrderItem> items) {
		if (items.isEmpty()) {
			throw new OrderValidationException(MSG_CANNOT_CREATE_EMPTY_ITEMS);
		}

		return items.stream()
					.map(item -> new OrderLineItem(this, item.getMenu(), item.getQuantity()))
					.collect(Collectors.toList());
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (this.orderStatus == OrderStatus.COMPLETION) {
			throw new OrderValidationException(MSG_CANNOT_CHANGE_COMPLETION);
		}
		this.orderStatus = orderStatus;
	}

	boolean isOngoing() {
		return orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL;
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

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
}
