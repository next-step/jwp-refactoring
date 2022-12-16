package kitchenpos.domain;

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

import kitchenpos.exception.CannotChangeEmptyOrderTable;
import kitchenpos.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.exception.InvalidNumberOfGuestsException;

@Entity
@Table(name = "order_table")
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL)
	private List<Order> orderList = new ArrayList<>();

	private Integer numberOfGuests;

	private Boolean empty;

	protected OrderTable() {
	}

	public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		setTableGroup(tableGroup);
	}

	public OrderTable(Long id, int numberOfGuests, Boolean empty) {
		this(id, null, numberOfGuests, empty);
	}

	public OrderTable(int numberOfGuests, Boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public void setTableGroup(TableGroup newTableGroup) {
		if (Objects.nonNull(tableGroup)) {
			tableGroup.getOrderTables().remove(this);
		}
		tableGroup = newTableGroup;
		if (Objects.isNull(tableGroup)) {
			return;
		}
		if (!tableGroup.getOrderTables().contains(this)) {
			tableGroup.getOrderTables().add(this);
		}
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void detachTableGroup() {
		tableGroup = null;
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
		if (!empty) {
			throw new CannotChangeNumberOfGuestsException();
		}
	}

	public void validateChangeEmpty() {
		if (hasNotCompletedOrder()) {
			throw new CannotChangeEmptyOrderTable();
		}
	}

	private boolean hasNotCompletedOrder() {
		return !orderList.isEmpty() && !orderList.stream().allMatch(Order::isCompleted);
	}
}
