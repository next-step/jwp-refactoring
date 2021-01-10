package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.exception.AlreadyOrderCompleteException;
import kitchenpos.exception.EmptyTableException;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems = new OrderLineItems();

	protected Order() {
	}

	private Order(OrderTable orderTable) {
		validateOrderTable(orderTable);
		this.orderTable = orderTable;
		this.orderStatus = OrderStatus.COOKING;
		this.orderedTime = LocalDateTime.now();
	}

	public static Order create(OrderTable orderTable) {
		return new Order(orderTable);
	}

	public void changeOrderStatus(final OrderStatus orderStatus) {
		validateBeforeChangeStatus();
		this.orderStatus = orderStatus;
	}

	public void addOrderLineItems(OrderLineItem orderLineItem) {
		orderLineItems.add(orderLineItem);
	}

	private void validateOrderTable(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new EmptyTableException("빈 테이블일 경우 주문을 진행할 수 없습니다.");
		}
	}

	private void validateBeforeChangeStatus() {
		if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
			throw new AlreadyOrderCompleteException("이미 완료된 주문입니다.");
		}
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

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.getOrderLineItems();
	}
}
