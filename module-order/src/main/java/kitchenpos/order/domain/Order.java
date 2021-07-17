package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "ordered_time")
	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "order")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected Order() {
	}

	public static Order ofCooking(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
		return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
	}

	public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		validateEmptyOrderTable(orderTable);
		validateEmptyOrderItems(orderLineItems);

		for (OrderLineItem orderLineItem : orderLineItems) {
			addOrderLineItem(orderLineItem);
		}
		this.order(orderTable);
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	private void order(OrderTable orderTable) {
		this.orderTable = orderTable;
		orderTable.addOrder(this);
	}

	private void validateEmptyOrderTable(OrderTable orderTable) {
		if (orderTable == null || orderTable.isEmpty()) {
			throw new IllegalArgumentException("비어있는 테이블은 주문할 수 없습니다.");
		}
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	private void validateEmptyOrderItems(List<OrderLineItem> orderLineItems) {
		if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
			throw new IllegalArgumentException("주문 항목을 구성해야 주문이 가능합니다.");
		}
	}

	public void addOrderLineItem(OrderLineItem orderLineItem) {
		this.orderLineItems.add(orderLineItem);
		orderLineItem.changeOrder(this);
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

	public Long getId() {
		return id;
	}

	private void validateChangeStatus() {
		if (OrderStatus.isComplete(orderStatus)) {
			throw new IllegalArgumentException("이미 완료된 주문입니다.");
		}
	}

	public void changeState(OrderStatus orderStatus) {
		this.validateChangeStatus();
		this.orderStatus = orderStatus;
	}

	public boolean isUnChangeable() {
		return OrderStatus.isUnChangeable(orderStatus);
	}

	public Long getOrderTableId() {
		if (orderTable == null) {
			return null;
		}
		return orderTable.getId();
	}
}
