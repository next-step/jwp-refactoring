package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Table(name = "orders")
@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
	private Long orderTableId;

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

	private Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	private static Order of(Long id, Long orderTableId, OrderLineItems orderLineItems) {
		validate(orderTableId);
		return new Order(id, orderTableId, OrderStatus.COOKING, orderLineItems);
	}

	public static Order create(Long orderTableId) {
		return of(null, orderTableId, OrderLineItems.empty());
	}

	private static void validate(Long orderTableId) {
		if (Objects.isNull(orderTableId)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "주문 테이블은 입력되어야 합니다");
		}
	}

	public static Order of(Long id, Long orderTableId, OrderStatus orderStatus,
		List<OrderLineItem> orderLineItemList) {
		return new Order(id, orderTableId, orderStatus, OrderLineItems.of(orderLineItemList));
	}

	public static Order of(long id, Long orderTableId, OrderStatus orderStatus) {
		return new Order(id, orderTableId, orderStatus, OrderLineItems.empty());
	}

	public void addOrderLineItems(List<OrderLineItem> orderLineItemList) {
		orderLineItemList.forEach(this::addOrderLineItem);
	}

	private void addOrderLineItem(OrderLineItem orderLineItem) {
		orderLineItem.setOrder(this);
		this.orderLineItems.add(orderLineItem);
	}

	public void updateStatus(OrderStatus updateStatusName) {
		validateUpdate(orderStatus);
		this.orderStatus = updateStatusName;
	}

	private void validateUpdate(OrderStatus orderStatus) {
		if (orderStatus.equals(OrderStatus.COMPLETION)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "이미 완료되어서, 상태를 바꿀 수 없습니다");
		}
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
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
