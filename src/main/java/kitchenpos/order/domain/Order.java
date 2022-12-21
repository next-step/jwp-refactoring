package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "order_table_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_order_order_table"))
	@OneToOne(optional = false)
	private OrderTable orderTable;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.COOKING;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	private Order(
		OrderTable orderTable,
		OrderLineItems orderLineItems
	) {
		Assert.notNull(orderTable, "주문 테이블은 필수입니다.");
		Assert.isTrue(orderTable.isFull(), "주문을 하는 테이블은 비어있을 수 없습니다.");
		Assert.notNull(orderLineItems, "주문 항목들은 필수입니다.");
		Assert.isTrue(orderLineItems.isNotEmpty(), "주문 항목들이 비어있을 수 없습니다.");
		this.orderTable = orderTable;
		orderLineItems.updateOrder(this);
		this.orderLineItems = orderLineItems;
	}

	public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
		return new Order(orderTable, orderLineItems);
	}

	public Long id() {
		return id;
	}

	public OrderTable table() {
		return orderTable;
	}

	public OrderStatus orderStatus() {
		return orderStatus;
	}

	public void updateStatus(final OrderStatus orderStatus) {
		if (this.orderStatus.isCompleted()) {
			throw new IllegalArgumentException("이미 완료된 주문은 상태를 변경할 수 없습니다.");
		}
		this.orderStatus = orderStatus;
	}

	public LocalDateTime orderedTime() {
		return orderedTime;
	}

	public List<OrderLineItem> orderLineItems() {
		return orderLineItems.list();
	}
}
