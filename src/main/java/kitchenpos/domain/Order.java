package kitchenpos.domain;

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

import org.springframework.util.CollectionUtils;

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

	public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {

		validateEmptyTableOrder(orderTable);
		ValidateEmptyOrderItems(orderLineItems);

		for (OrderLineItem orderLineItem : orderLineItems) {
			addOrderLineItem(orderLineItem);
		}
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	private void ValidateEmptyOrderItems(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException("주문 항목을 구성해야 주문이 가능합니다.");
		}
	}

	private void validateEmptyTableOrder(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException("비어있는 테이블은 주문할 수 없습니다.");
		}
	}

	public void addOrderLineItem(OrderLineItem orderLineItem) {
		this.orderLineItems.add(orderLineItem);
		orderLineItem.changeOrder(this);
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

	public Long getId() {
		return id;
	}

	private void validateChangeStatus() {
		if (Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus().name())) {
			throw new IllegalArgumentException("이미 완료된 주문입니다.");
		}
	}

	public void changeState(OrderStatus orderStatus) {
		this.validateChangeStatus();
		this.orderStatus = orderStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order)o;
		return Objects.equals(id, order.id) && Objects.equals(orderTable, order.orderTable)
			&& orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime)
			&& Objects.equals(orderLineItems, order.orderLineItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
	}
}
