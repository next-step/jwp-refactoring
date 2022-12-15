package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.exception.InvalidNumberOfGuestsException;

@Entity
@Table(name = "order_table")
public class OrderTable2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "table_group_id")
	private TableGroup2 tableGroup;

	private Integer numberOfGuests;

	private Boolean empty;

	protected OrderTable2() {
	}

	public OrderTable2(Long id, TableGroup2 tableGroup, int numberOfGuests, Boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		setTableGroup(tableGroup);
	}

	public OrderTable2(Long id, int numberOfGuests, Boolean empty) {
		this(id, null, numberOfGuests, empty);
	}

	public OrderTable2(int numberOfGuests, Boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public void setTableGroup(TableGroup2 newTableGroup) {
		if (Objects.isNull(newTableGroup)) {
			return;
		}
		if (Objects.nonNull(tableGroup)) {
			tableGroup.getOrderTables().remove(this);
		}
		tableGroup = newTableGroup;
		if (!tableGroup.getOrderTables().contains(this)) {
			tableGroup.getOrderTables().add(this);
		}
	}

	public Long getId() {
		return id;
	}

	public TableGroup2 getTableGroup() {
		return tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean isEmpty() {
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

	public void changeNumberOfGuests(OrderTable2 other) {
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
}
