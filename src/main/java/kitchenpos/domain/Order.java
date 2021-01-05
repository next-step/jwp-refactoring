package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore //memo [2021-01-4 22:14] 수정필요
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;
	private String orderStatus;
	private LocalDateTime orderedTime;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems;

	protected Order() {
	}

	private Order(OrderTable orderTable) {
		this.orderTable = orderTable;
		this.orderStatus = OrderStatus.COOKING.name();
		this.orderedTime = LocalDateTime.now();
	}

	public static Order create(OrderTable orderTable) {
		return new Order(orderTable);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(OrderTable orderTable) {
		this.orderTable = orderTable;
	}

   /* public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }*/

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
}
