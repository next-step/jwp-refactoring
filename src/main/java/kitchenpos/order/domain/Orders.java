package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	private String orderStatus;

	private LocalDateTime orderedTime;

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public Orders() {
	}

	public Orders(OrderTable orderTable, String orderStatus, List<OrderLineItem> orderLineItems) {
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
		setOrder();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}


	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(final String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public void setOrderedTime(final LocalDateTime orderedTime) {
		this.orderedTime = orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(OrderTable orderTable) {
		this.orderTable = orderTable;
	}

	@PrePersist
	public void prePersist() {
		this.orderedTime = LocalDateTime.now();
	}

	private void setOrder() {
		this.orderTable.setOrder(this);
		this.orderLineItems.forEach(orderLineItem -> {
			orderLineItem.setOrder(this);
		});
	}

	public void changeStatus(String orderStatus) {
		if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
			throw new IllegalArgumentException("계산완료 주문인 경우, 상태를 변경할 수 없습니다");
		}
		this.orderStatus = orderStatus;
	}
}
