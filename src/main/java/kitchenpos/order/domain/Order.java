package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.domain.OrderStatus;
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
	private LocalDateTime orderedTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	private List<OrderLineItem> orderLineItems;

	protected Order() {
	}

	private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
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
