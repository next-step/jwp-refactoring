package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseIdEntity {

	@ManyToOne
	@JoinColumn(name = "order_table_id", nullable = false)
	private OrderTable orderTable;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	@CreatedDate
	@Column(name = "ordered_time", nullable = false)
	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "order")
	private List<OrderLineItem> orderLineItems;

	protected Order() {
	}

	public static Order createCookingOrder(OrderTable orderTable) {
		return new Order(orderTable, OrderStatus.COOKING);
	}

	private Order(OrderTable orderTable, OrderStatus orderStatus) {
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = new ArrayList<>();
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
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

	public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAll(orderLineItems);
	}

	public void addOrderLineItem(OrderLineItem orderLineItem) {
		this.orderLineItems.add(orderLineItem);
	}
}
