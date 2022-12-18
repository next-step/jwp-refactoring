package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.table.exception.InvalidNumberOfGuestsException;

@Entity
@Table(name = "order_table")
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	private Integer numberOfGuests;

	private Boolean empty;

	protected OrderTable() {
	}

	public OrderTable(Long id, TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(Long id, int numberOfGuests, Boolean empty) {
		this(id, null, numberOfGuests, empty);
	}

	public OrderTable(int numberOfGuests, Boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
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
		if (!isEmpty()) {
			throw new CannotChangeNumberOfGuestsException();
		}
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
