package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import kitchenpos.BaseEntity;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private OrderTable orderTable;
	private String orderStatus = OrderStatus.COOKING.name();
	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	private Order(OrderTable orderTable, String orderStatus, List<OrderLineItem> orderLineItems) {
		validate(orderTable);
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = new OrderLineItems(this, orderLineItems);

	}

	private void validate(OrderTable orderTable) {
		if (Objects.isNull(orderTable)) {
			throw new IllegalArgumentException("주문 테이블이 없습니다.");
		}
	}

	public static OrderBuilder builder() {
		return new OrderBuilder();
	}

	public Long getId() {
		return id;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void changeOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public boolean isOrderStatus(String orderStatus) {
		return this.orderStatus.equals(orderStatus);
	}

	public boolean containsOrderStatus(List<String> orderStatuses) {
		return orderStatuses.contains(orderStatus);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.getList();
	}

	public static final class OrderBuilder {
		private OrderTable orderTable;
		private String orderStatus = OrderStatus.COOKING.name();
		private List<OrderLineItem> orderLineItems;

		private OrderBuilder() {
		}

		public OrderBuilder orderTable(OrderTable orderTable) {
			this.orderTable = orderTable;
			return this;
		}

		public OrderBuilder orderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
			return this;
		}

		public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
			this.orderLineItems = orderLineItems;
			return this;
		}

		public Order build() {
			return new Order(orderTable, orderStatus, orderLineItems);
		}
	}
}
