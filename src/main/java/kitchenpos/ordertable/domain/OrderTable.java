package kitchenpos.ordertable.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.domain.TableGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.exception.TableException;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;

	private boolean empty;

	public OrderTable() {

	}

	public OrderTable(NumberOfGuests numberOfGuests) {
		this.tableGroup = null;
		this.numberOfGuests = numberOfGuests;
		this.empty = true;
	}

	public OrderTable(long id, NumberOfGuests numberOfGuests) {
		this(numberOfGuests);
		this.id = id;
	}

	public OrderTable(long id, NumberOfGuests numberOfGuests, boolean isEmpty) {
		this(id, numberOfGuests);
		this.empty = isEmpty;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public Long getId() {
		return id;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void changeEmpty(boolean isEmpty, Order order) {
		if (Objects.nonNull(tableGroup)) {
			throw new TableException("단체 지정이 존재하여 변경할 수 없습니다.");
		}
		if (Objects.isNull(order)) {
			throw new TableException("주문이 존재하지 않아 변경할 수 없습니다.");
		}
		if (!order.getOrderStatus().equals(OrderStatus.COMPLETION)) {
			throw new TableException("주문의 상태가 조리중이거나 식사중일 경우 변경할 수 없습니다.");
		}
		this.empty = isEmpty;
	}

	public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
		if (empty) {
			throw new TableException("주문 테이블이 비워져있어 손님 수를 변경할 수 없습니다.");
		}
		this.numberOfGuests = numberOfGuests;
	}
}
