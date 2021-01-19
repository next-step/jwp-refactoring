package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.NumberOfGuests;
import kitchenpos.common.TableValidationException;

import javax.persistence.*;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseIdEntity {

	static final String MSG_TABLE_GROUP_NOT_NULL = "tableGroup must not be null";

	@ManyToOne
	@JoinColumn(name = "table_group_id", nullable = true)
	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Column(name = "empty", nullable = false)
	private boolean empty;

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this(numberOfGuests, empty, null);
	}

	private OrderTable(int numberOfGuests, boolean empty, TableGroup tableGroup) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
		this.empty = empty;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public void putIntoGroup(TableGroup tableGroup) {
		if (tableGroup == null) {
			// TODO: 2021-01-19 테스트 작성
			throw new TableValidationException(MSG_TABLE_GROUP_NOT_NULL);
		}
		this.tableGroup = tableGroup;
	}

	public void ungroup() {
		this.tableGroup = null;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public void changeNumberOfGuests(final int numberOfGuests) {
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
	}

	public boolean isEmpty() {
		return empty;
	}

	public void changeEmpty(final boolean empty) {
		this.empty = empty;
	}
}
