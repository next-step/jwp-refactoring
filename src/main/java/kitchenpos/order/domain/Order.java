package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

import org.hibernate.annotations.CreationTimestamp;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;

@Table(name = "orders")
@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderTable orderTable;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	private static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
		validate(orderTable, orderStatus);
		return new Order(id, orderTable, orderStatus, orderLineItems);
	}

	public static Order create(OrderTable orderTable) {
		return of(null, orderTable, OrderStatus.COOKING, OrderLineItems.empty());
	}

	private static void validate(OrderTable orderTable, OrderStatus orderStatus) {
		if (Objects.isNull(orderTable)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "주문 테이블은 입력되어야 합니다");
		}
		if (Objects.isNull(orderStatus)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "주문 상태는 입력되어야 합니다");
		}
	}

	public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus,
		List<OrderLineItem> orderLineItemList) {
		return new Order(id, orderTable, orderStatus, OrderLineItems.of(orderLineItemList));
	}

	public static Order of(long id, OrderTable orderTable, OrderStatus orderStatus) {
		return new Order(id, orderTable, orderStatus, OrderLineItems.empty());
	}

	public void addOrderLineItems(List<OrderLineItem> orderLineItemList) {
		orderLineItemList.forEach(this::addOrderLineItem);
	}

	private void addOrderLineItem(OrderLineItem orderLineItem) {
		orderLineItem.setOrder(this);
		this.orderLineItems.add(orderLineItem);
	}

	public void updateStatus(String updateStatusName) {
		if (orderStatus.equals(OrderStatus.COMPLETION)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "이미 완료되어서, 상태를 바꿀 수 없습니다");
		}
		this.orderStatus = OrderStatus.valueOf(updateStatusName);
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
		return id.hashCode();
	}

}
