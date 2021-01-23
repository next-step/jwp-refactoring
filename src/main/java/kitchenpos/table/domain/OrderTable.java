package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	private int numberOfGuests;
	private boolean empty;

	public OrderTable() {
	}

	public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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

	public void changeTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public void changeEmpty(boolean empty) {
		this.empty = empty;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderTable that = (OrderTable)o;
		return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty() && Objects.equals(
			getId(), that.getId()) && Objects.equals(getTableGroup(), that.getTableGroup());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTableGroup(), getNumberOfGuests(), isEmpty());
	}

	@Override
	public String toString() {
		return "OrderTable{" +
			"id=" + id +
			", tableGroup=" + tableGroup +
			", numberOfGuests=" + numberOfGuests +
			", empty=" + empty +
			'}';
	}
}
