package kitchenpos.orders.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_table_id", nullable = false,columnDefinition = "bigint(20)")
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems;

	public Order() {
	}

	public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		this(null, orderTable, orderStatus, orderLineItems);
	}

	public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = setOrder(orderLineItems);
	}

	public boolean isCompletion() {
		return Objects.equals(OrderStatus.COMPLETION, this.orderStatus);
	}

	public void changeOrderStatus(String orderStatus) {
		this.orderStatus = OrderStatus.valueOf(orderStatus);
	}

	private List<OrderLineItem> setOrder(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(ol -> new OrderLineItem(this, ol.getMenu(), ol.getQuantity()))
			.collect(Collectors.toList());
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
		return orderLineItems;
	}

	public Long getOrderTableId() {
		return this.orderTable.getId();
	}

}
