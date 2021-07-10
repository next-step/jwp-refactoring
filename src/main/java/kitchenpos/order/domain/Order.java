package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private OrderTable orderTable;

	@Embedded
	private OrderLineItems orderLineItems;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private LocalDateTime orderedTime;

	//todo protected
	public Order() {

	}

	public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
		validate(orderTable);
		this.orderTable = orderTable;
		this.orderLineItems = orderLineItems;
		this.orderStatus = OrderStatus.COOKING;
		this.orderedTime = LocalDateTime.now();
	}

	public Order(long id, OrderTable orderTable, OrderLineItems orderLineItems) {
		this(orderTable, orderLineItems);
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

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
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
