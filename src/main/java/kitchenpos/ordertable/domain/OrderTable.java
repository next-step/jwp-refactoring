package kitchenpos.ordertable.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;

@Table(name = "order_table")
@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "table_group_id")
	private OrderTableGroup orderTableGroup;

	@OneToOne(mappedBy = "orderTable")
	private Order order;

	@Embedded
	private NumberOfGuests numberOfGuests;

	private boolean empty;

	protected OrderTable() {
	}

	public static OrderTable of(NumberOfGuests numberOfGuests, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.numberOfGuests = numberOfGuests;
		orderTable.empty = empty;
		return orderTable;
	}

	public Long getId() {
		return id;
	}

	public OrderTableGroup getOrderTableGroup() {
		return orderTableGroup;
	}

	public Order getOrder() {
		return order;
	}

	public Long getOrderTableGroupId() {
		return orderTableGroup != null ? orderTableGroup.getId() : null;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean hasOrderTableGroup() {
		return orderTableGroup != null;
	}

	public void changeEmpty(boolean empty) {
		if (hasOrderTableGroup()) {
			throw new IllegalStateException("주문 테이블 그룹에 속해 있으면 빈 상태를 변경할 수 없습니다.");
		}

		if (order != null && !order.isCompleted()) {
			throw new IllegalStateException("완료되지 않은 주문이 남아 있는 경우 빈 상태를 변경할 수 없습니다.");
		}

		this.empty = empty;
	}

	public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
		if (isEmpty()) {
			throw new IllegalStateException("주문 테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
		}

		this.numberOfGuests = numberOfGuests;
	}

	public void setOrderTableGroup(OrderTableGroup orderTableGroup) {
		this.orderTableGroup = orderTableGroup;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
