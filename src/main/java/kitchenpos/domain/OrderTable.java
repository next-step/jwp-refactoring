package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.NumberOfGuests;
import kitchenpos.common.TableValidationException;

import javax.persistence.*;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseIdEntity {

	static final String MSG_TABLE_GROUP_NOT_NULL = "tableGroup must not be null";
	static final String MSG_ORDER_TABLE_ALREADY_GROUP = "OrderTable already has TableGroup";
	static final String MSG_ORDER_TABLE_ONGOING = "OrderTable's OrderStatus is ongoing";
	static final String MSG_ORDER_TABLE_EMPTY = "OrderTable must be empty";
	static final String MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP = "Cannot change empty of already in TableGroup";
	static final String MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER = "Cannot change empty while Order is ongoing";
	static final String MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY = "Cannot change guests while OrderTable is empty";

	@ManyToOne
	@JoinColumn(name = "table_group_id", nullable = true)
	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Column(name = "empty", nullable = false)
	private boolean empty;

	// TODO: 2021-01-21 Order 를 OneToMany 로 소유

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

	public void changeEmpty(boolean empty) {
		if (this.tableGroup != null) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP);
		}

		if (hasOngoingOrder()) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER);
		}

		this.empty = empty;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		if (this.empty) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY);
		}
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
	}

	void putIntoGroup(TableGroup tableGroup) {
		if (tableGroup == null) {
			throw new TableValidationException(MSG_TABLE_GROUP_NOT_NULL);
		}

		if (this.tableGroup != null) {
			throw new TableValidationException(MSG_ORDER_TABLE_ALREADY_GROUP);
		}

		if (!this.empty) {
			throw new TableValidationException(MSG_ORDER_TABLE_EMPTY);
		}

		this.tableGroup = tableGroup;
		this.empty = false;
	}

	void ungroup() {
		if (hasOngoingOrder()) {
			throw new TableValidationException(MSG_ORDER_TABLE_ONGOING);
		}
		this.tableGroup = null;
	}

	private boolean hasOngoingOrder() {
		// TODO: 2021-01-21 주문테이블 상태비교
		return false;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
