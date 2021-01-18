package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;

import javax.persistence.*;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseIdEntity {

	@ManyToOne
	@JoinColumn(name = "table_group_id", nullable = true)
	private TableGroup tableGroup;

	@Column(name = "numberOfGuests", nullable = false)
	private int numberOfGuests;

	@Column(name = "empty", nullable = false)
	private boolean empty;

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this(numberOfGuests, empty, null);
	}

	private OrderTable(int numberOfGuests, boolean empty, TableGroup tableGroup) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public void putIntoGroup(TableGroup tableGroup) {
		if (tableGroup == null) {
			throw new IllegalArgumentException();
		}
		this.tableGroup = tableGroup;
	}

	public void ungroup() {
		this.tableGroup = null;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void changeNumberOfGuests(final int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void changeEmpty(final boolean empty) {
		this.empty = empty;
	}
}
