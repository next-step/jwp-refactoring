package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	private Order(OrderTable orderTable, OrderLineItems orderLineItems) {
		throwOnEmptyOrderTable(orderTable);

		this.orderTable = orderTable;
		this.orderStatus = OrderStatus.COOKING;
		this.orderLineItems = orderLineItems;

		this.orderTable.setOrder(this);
	}

	public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
		return new Order(orderTable, orderLineItems);
	}

	private void throwOnEmptyOrderTable(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException("주문 테이블이 비어있으면 주문을 생성할 수 없습니다.");
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

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (this.orderStatus.isCompletion()) {
			throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
		}

		this.orderStatus = orderStatus;
	}

	public boolean isCompleted() {
		return orderStatus.isCompletion();
	}
}
