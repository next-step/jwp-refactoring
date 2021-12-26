package kitchenpos.ordertable.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	@Embedded
	private NumberOfGuests numberOfGuests;

	private boolean empty;

	protected OrderTable() {
	}

	public static OrderTable of(Long id, NumberOfGuests numberOfGuests, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.id = id;
		orderTable.numberOfGuests = numberOfGuests;
		orderTable.empty = empty;
		return orderTable;
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

	public Long getOrderTableGroupId() {
		if (orderTableGroup != null) {
			return orderTableGroup.getId();
		}

		return null;
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

	public void changeEmpty(boolean empty, OrderTableValidator validator) {
		if (hasOrderTableGroup()) {
			throw new IllegalStateException("주문 테이블 그룹에 속해 있으면 빈 상태를 변경할 수 없습니다.");
		}

		validator.validateNotCompletedOrderNotExist(id);

		this.empty = empty;
	}

	public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
		if (isEmpty()) {
			throw new IllegalStateException("주문 테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
		}

		this.numberOfGuests = numberOfGuests;
	}

	public void groupedBy(OrderTableGroup orderTableGroup) {
		this.orderTableGroup = orderTableGroup;
		this.empty = false;
	}

	public void ungrouped() {
		this.orderTableGroup = null;
	}
}
