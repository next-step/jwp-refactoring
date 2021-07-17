package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.table.domain.OrderTable;

@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_orders_order_table"))
	@Column(nullable = false)
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	private String orderStatus;

	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems;

	public Order() {
	}

	public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public Long getId() {
		return id;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public boolean isStatusChangeable() {
		return Objects.equals(OrderStatus.COMPLETION.name(), orderStatus);
	}

	public void updateStatus(String status) {
		this.orderStatus = status;
	}
}
