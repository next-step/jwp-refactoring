package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.CannotChangeEmptyOrderTable;
import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.table.exception.CannotDetachTableGroupException;
import kitchenpos.table.exception.InvalidNumberOfGuestsException;

@Entity
@Table(name = "order_table")
public class OrderTable {

	@OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL)
	private final List<Order> orderList = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;
	private Integer numberOfGuests;

	private Boolean empty;

	protected OrderTable() {
	}

	public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		changeTableGroup(tableGroup);
	}

	public OrderTable(Long id, int numberOfGuests, Boolean empty) {
		this(id, null, numberOfGuests, empty);
	}

	public OrderTable(int numberOfGuests, Boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public void changeTableGroup(TableGroup newTableGroup) {
		if (Objects.nonNull(tableGroup)) {
			tableGroup.getOrderTables()
					  .remove(this);
		}
		tableGroup = newTableGroup;
		if (Objects.isNull(tableGroup)) {
			return;
		}
		if (!tableGroup.getOrderTables()
					   .contains(this)) {
			tableGroup.getOrderTables()
					  .add(this);
		}
	}

	public Long getId() {
		return id;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void detachTableGroup() {
		validateDetachTableGroup();
		tableGroup = null;
	}

	private void validateDetachTableGroup() {
		if (hasAnyNotCompletedOrder()) {
			throw new CannotDetachTableGroupException();
		}
	}

	public boolean hasTableGroup() {
		return Objects.nonNull(tableGroup);
	}

	public void changeEmpty(boolean empty) {
		this.empty = empty;
	}

	public void changeNumberOfGuests(OrderTable other) {
		other.validateNumberOfGuests();
		validateNotEmptyTable();
		this.numberOfGuests = other.numberOfGuests;
	}

	public void validateNumberOfGuests() {
		if (numberOfGuests < 0) {
			throw new InvalidNumberOfGuestsException();
		}
	}

	private void validateNotEmptyTable() {
		if (!isEmpty()) {
			throw new CannotChangeNumberOfGuestsException();
		}
	}

	public void validateChangeEmpty() {
		if (hasAnyNotCompletedOrder()) {
			throw new CannotChangeEmptyOrderTable();
		}
	}

	private boolean hasAnyNotCompletedOrder() {
		return !orderList.isEmpty() && !orderList.stream()
												 .allMatch(Order::isCompleted);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderTable that = (OrderTable)o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
