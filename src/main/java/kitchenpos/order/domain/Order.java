package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
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

import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private LocalDateTime orderedTime;

	protected Order() {

	}

	public Order(OrderTable orderTable) {
		validate(orderTable);
		this.orderTable = orderTable;
		this.orderStatus = OrderStatus.COOKING;
		this.orderedTime = LocalDateTime.now();
	}

	public Order(long id, OrderTable orderTable) {
		this(orderTable);
		this.id = id;
	}

	private void validate(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new OrderException("주문 테이블이 비워져있어 주문을 생성할 수 없습니다.");
		}
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (this.orderStatus == OrderStatus.COMPLETION) {
			throw new OrderException("계산완료된 주문은 상태를 변경할 수 없습니다.");
		}
		this.orderStatus = orderStatus;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

}
