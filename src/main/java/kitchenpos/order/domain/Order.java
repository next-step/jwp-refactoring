package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_table_id")
	private Long orderTableId;

	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "ordered_time")
	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "order")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected Order() {
	}

	public static Order ofCooking(Long orderTableId, List<OrderLineItem> orderLineItems) {
		return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
	}

	public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {

		validateEmptyOrderItems(orderLineItems);

		for (OrderLineItem orderLineItem : orderLineItems) {
			addOrderLineItem(orderLineItem);
		}
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	private void validateEmptyOrderItems(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
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
}
