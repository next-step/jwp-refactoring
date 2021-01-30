package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.BaseEntity;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	protected Orders() {
	}

	private Orders(final Long id, final OrderTable orderTable, final OrderStatus orderStatus) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
	}

	public static Orders of(final Long id, final OrderTable orderTable, final OrderStatus orderStatus) {
		return new Orders(id, orderTable, orderStatus);
	}

	public static Orders of(OrderTable orderTable) {
		return of(null, orderTable, OrderStatus.COOKING);
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

	public String orderStatus() {
		return this.orderStatus.name();
	}

	public void changeOrderStatus(OrderStatus newStatus) {
		validateCompleteStatus();
		this.orderStatus = newStatus;
	}

	public void validateStatus() {
		if (this.orderStatus.equals(OrderStatus.COOKING) || this.orderStatus.equals(OrderStatus.MEAL)) {
			throw new IllegalArgumentException("조리중이나, 식사중에는 상태를 변경할 수 없습니다.");
		}
	}

	private void validateCompleteStatus() {
		if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
			throw new IllegalArgumentException("조리 상태가 완료일 경우 변경할 수 없습니다.");
		}
	}
}
